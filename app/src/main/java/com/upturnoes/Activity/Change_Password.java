package com.upturnoes.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.upturnoes.Class.MyConfig;
import com.upturnoes.MainActivity;
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

public class Change_Password extends AppCompatActivity {

    private View parent_view;
    EditText oldPass,newPass,re_nrePass;
    Button Submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    Dialog dialog;
    EditText text_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        parent_view = findViewById(android.R.id.content);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        oldPass=(EditText)findViewById(R.id.editOldpass);
        newPass=(EditText)findViewById(R.id.editNewPass);
        re_nrePass=(EditText)findViewById(R.id.editRenewpass);
        Submit=(Button)findViewById(R.id.buttonPISubmit);
        Submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    // Toast.makeText(Activity_Change_Password.this,"Enter right data!!!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(parent_view, "Enter right data!!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }else {
                    SavePassWord();
                }
            }
        } );
        Tools.setSystemBarColor(this);
    }

    public boolean validate() {
        boolean valid = true;

        String old_password = oldPass.getText().toString();
        String new_password = newPass.getText().toString();
        String reEnterPassword = re_nrePass.getText().toString();
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String oldpass_tag= sharedPreferences.getString("password",null);
        Log.e("thanks", oldpass_tag+"//"+old_password + "/////" + new_password + "////" + reEnterPassword );
        if (old_password.isEmpty() || !(old_password.equals(oldpass_tag))){
            oldPass.setError("Old Password not match");
            valid = false;
        } else {
            oldPass.setError(null);
        }
        if (new_password.isEmpty() || new_password.length() < 4 || new_password.length() > 10) {
            newPass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            newPass.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(new_password))) {
            re_nrePass.setError("Password Do not match");
            valid = false;
        } else {
            re_nrePass.setError(null);
        }

        return valid;
    }
    private void emptyInputEditText() {
        oldPass.setText(null);
        newPass.setText(null);
        re_nrePass.setText(null);
    }
    private void SavePassWord() {
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        final String student_id= sharedPreferences.getString("idtag",null);
        final String passwordlog= sharedPreferences.getString("password",null);
        final String new_pass = newPass.getText().toString();

        nameValuePairs.add(new BasicNameValuePair("new_pass", new_pass));
        nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
        nameValuePairs.add(new BasicNameValuePair("passwordlog", passwordlog));

        Log.e("new_pass", new_pass);

        nameValuePairs.add(new BasicNameValuePair("new_pass", new_pass));
        nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
        nameValuePairs.add(new BasicNameValuePair("passwordlog", passwordlog));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(MyConfig.URL_UPDATE_PASSWORD);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            String data = EntityUtils.toString(entity);
            Log.e("Register", data);
            if(data.matches( "success" )) {
                Log.e( "pass 1", "connection success " );
                Toast.makeText( Change_Password.this, "Password Change Successfully", Toast.LENGTH_SHORT ).show();
                emptyInputEditText();
                sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
                editor = sharedPreferences.edit();
                editor.remove( "user_name" );
                editor.remove( "password" );
                editor.commit();
                Intent i = new Intent(Change_Password.this, Login.class);
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
            if(data.matches( "failure" )) {
                //  if(data.matches( "phone number already exits" )) {
                Toast.makeText( Change_Password.this, "password not updated,something went wrong", Toast.LENGTH_SHORT ).show();
                //   }
            }
        } catch (ClientProtocolException e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
