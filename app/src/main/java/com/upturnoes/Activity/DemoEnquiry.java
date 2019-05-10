package com.upturnoes.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DemoEnquiry extends AppCompatActivity {

    EditText edit_your_name,edit_org_name,edit_contact,edit_city,edit_email,edit_website,edit_address,edit_courses,edit_message;
    Button bt_submit;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_enquiry);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();

        edit_your_name = (EditText) findViewById(R.id.edit_your_name);
        edit_org_name = (EditText) findViewById(R.id.edit_org_name);
        edit_contact = (EditText) findViewById(R.id.edit_contact);
        edit_city = (EditText) findViewById(R.id.edit_city);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_website = (EditText) findViewById(R.id.edit_website);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_courses = (EditText) findViewById(R.id.edit_courses);
        edit_message = (EditText) findViewById(R.id.edit_message);

        bt_submit = (Button) findViewById(R.id.bt_submit);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    AddEnquiry();
                }
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Demo & Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validate() {
        boolean valid = true;

        String fullname = edit_your_name.getText().toString();
        String contact = edit_contact.getText().toString();
        String email = edit_email.getText().toString();
        String message = edit_message.getText().toString();
        String courses = edit_courses.getText().toString();
        String org_name = edit_org_name.getText().toString();
        String city = edit_city.getText().toString();
        String address = edit_address.getText().toString();

        if (message.isEmpty()) {
            edit_message.setError("Enter Message");
            valid = false;
        } else {
            edit_message.setError(null);
        }
        if (fullname.isEmpty()) {
            edit_your_name.setError("Enter your name");
            valid = false;
        } else {
            edit_your_name.setError(null);
        }
        if (contact.isEmpty()) {
            edit_contact.setError("Enter Contact Number");
            valid = false;
        } else {
            edit_contact.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            edit_email.setError("Field required");
            valid = false;
        } else if (!isEmailValid(email)) {
            edit_email.setError("Invalid email");
            valid = false;
        }else {
            edit_email.setError(null);
        }

        if (courses.isEmpty()) {
            edit_courses.setError("Enter Courses");
            valid = false;
        } else {
            edit_courses.setError(null);
        }
        if (org_name.isEmpty()) {
            edit_org_name.setError("Enter Organization name ");
            valid = false;
        } else {
            edit_org_name.setError(null);
        }
        if (city.isEmpty()) {
            edit_city.setError("Enter City");
            valid = false;
        } else {
            edit_city.setError(null);
        }
        if (address.isEmpty()) {
            edit_address.setError("Enter Address");
            valid = false;
        } else {
            edit_address.setError(null);
        }
        return valid;
    }
    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void emptyInputEditText(){
         edit_your_name.setText(null);
         edit_org_name.setText(null);
         edit_contact.setText(null);
         edit_city.setText(null);
         edit_email.setText(null);
         edit_website.setText(null);
         edit_address.setText(null);
         edit_courses.setText(null);
         edit_message.setText(null);
    }

    private void AddEnquiry() {

        progressDialog = new ProgressDialog(DemoEnquiry.this,
                R.style.Dialog_Theme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String student_id= sharedPreferences.getString("idtag",null);

        String fullname = edit_your_name.getText().toString();
        String org_name = edit_org_name.getText().toString();
        String contact = edit_contact.getText().toString();
        String city = edit_city.getText().toString();
        String email = edit_email.getText().toString();
        String website = edit_website.getText().toString();
        String address = edit_address.getText().toString();
        String courses = edit_courses.getText().toString();
        String message = edit_message.getText().toString();

        nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
        nameValuePairs.add(new BasicNameValuePair("fullname", fullname));
        nameValuePairs.add(new BasicNameValuePair("org_name", org_name));
        nameValuePairs.add(new BasicNameValuePair("contact", contact));
        nameValuePairs.add(new BasicNameValuePair("city", city));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("website", website));
        nameValuePairs.add(new BasicNameValuePair("address", address));
        nameValuePairs.add(new BasicNameValuePair("courses", courses));
        nameValuePairs.add(new BasicNameValuePair("message", message));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_ADD_DEMO_ENQUIRY);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            String data = EntityUtils.toString(entity);
            Log.e("Register", data);
            if(data.matches( "success" )) {
                Log.e( "pass 1", "connection success " );

                progressDialog.dismiss();
                Toast.makeText( DemoEnquiry.this, "Send Successfully", Toast.LENGTH_SHORT ).show();

                 emptyInputEditText();

            }if(data.matches( "failure" )) {
                Log.e( "pass 1", "connection success " );
                progressDialog.dismiss();
                Toast.makeText( DemoEnquiry.this, "Something went wrong", Toast.LENGTH_SHORT ).show();

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
