package com.upturnoes.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.upturnoes.Class.BackgroundWorker;
import com.upturnoes.MainActivity;
import com.upturnoes.R;
import com.upturnoes.utils.Tools;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private View parent_view;
    TextInputEditText edit_username,edit_password;
    Button btn_sign_in;

    AppCompatCheckBox check_keepsigned;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        parent_view = findViewById(android.R.id.content);

        parent_view = findViewById(android.R.id.content);
        edit_username = (TextInputEditText) findViewById(R.id.username);
        edit_password = (TextInputEditText) findViewById(R.id.password);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        check_keepsigned = (AppCompatCheckBox) findViewById(R.id.check_keepsigned);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);


        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {
                // connected to wifi

                btn_sign_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
                        checkLogin(view);
                    }
                });

                ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Login.this, ForgotPWD.class);
                        startActivity(i);
                       // Snackbar.make(parent_view, "Sign up for an account", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Intent i = new Intent(Login.this, NoItemInternetImage.class);
            startActivity(i);
        }
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String usernamelog= sharedPreferences.getString("user_name",null);
        String passwordlog= sharedPreferences.getString("password",null);
        // String resultlog="2";
        Log.e("login",usernamelog +"/////"+passwordlog);
        if((!(usernamelog == null) )){
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // perform logic
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String username1= sharedPreferences.getString("username1",null);
        String password1= sharedPreferences.getString("password1",null);
        if((!(username1 == null) )){
            check_keepsigned.setChecked(true);
            edit_username.setText(username1);
            edit_password.setText(password1);
        }

        check_keepsigned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("username1", edit_username.getText().toString());
                    editor.putString("password1",  edit_password.getText().toString());
                    editor.commit();
                    Log.e("checked", edit_username.getText().toString() +"/////"+ edit_password.getText().toString());


                }

            }
        });


    }



    public void checkLogin(View arg0) {
        final String username = edit_username.getText().toString();

        final String pass = edit_password.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            edit_password.setError("Enter valid password");
        }
        String type = "login";
        // if(isValidEmail(username) && isValidPassword(pass))
        if(isValidPassword(pass))
        {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, username, pass);
        }
    }
    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }

}
