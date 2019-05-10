package com.upturnoes.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.upturnoes.Class.MyConfig;
import com.upturnoes.R;
import com.upturnoes.utils.Tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class VerificationCode extends AppCompatActivity {

    TextInputEditText code1,code2,code3,code4;
    AppCompatButton btn_verify;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        initToolbar();

        btn_verify = (AppCompatButton) findViewById(R.id.btn_verify);
        code1 = (TextInputEditText) findViewById(R.id.code1);
        code2 = (TextInputEditText) findViewById(R.id.code2);
        code3 = (TextInputEditText) findViewById(R.id.code3);
        code4 = (TextInputEditText) findViewById(R.id.code4);

         email = getIntent().getExtras().getString("email");


        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {


                btn_verify.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String digit1 = code1.getText().toString().trim();
                        final String digit2 = code2.getText().toString().trim();
                        final String digit3 = code3.getText().toString().trim();
                        final String digit4 = code4.getText().toString().trim();

                        final String final_code = digit1+""+digit2+""+digit3+""+digit4;


                        nameValuePairs.add(new BasicNameValuePair("final_code", final_code));
                        nameValuePairs.add(new BasicNameValuePair("email", email));

                        Log.e("final_code", final_code +"//"+ email);

                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost( MyConfig.VERIFICATION_CODE);
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            //  is = entity.getContent();
                            String data = EntityUtils.toString(entity);
                            Log.e("Verification", data);
                            if(data.matches( "success" )) {
                                Log.e( "pass 1", "connection success " );
                                // Toast.makeText( Activity_Forgot_Pwd.this, getResources().getString( R.string.chang_pass ), Toast.LENGTH_SHORT ).show();

                                Intent i = new Intent(VerificationCode.this,Login.class);
                                startActivity(i);
                                //finish();

                            } if(data.matches( "failure" )) {
                                //  if(data.matches( "phone number already exits" )) {
                                Toast.makeText( getApplicationContext(), "Verification code not match", Toast.LENGTH_SHORT ).show();
                                //   }
                            }
                        } catch (ClientProtocolException e) {
                            Log.e("Fail 1", e.toString());
                            Toast.makeText(getApplicationContext(), "Invalid IP connection", Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } );



            }
        } else {

            Intent i = new Intent(VerificationCode.this, NoItemInternetImage.class);
            startActivity(i);
        }


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
