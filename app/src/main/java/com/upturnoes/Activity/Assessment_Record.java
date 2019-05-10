package com.upturnoes.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Assessment_Record extends AppCompatActivity {

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView txt_exam_name,txt_student_name,txt_right_que,txt_wrong_que,txt_score,txt_result;
    String student_id,exam_id,exam_title,student_class,first_name,last_name;

    AppCompatButton btn_review,btn_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_assessment__record);
        initToolbar();
        txt_exam_name= (TextView) findViewById(R.id.txt_exam_name);
        txt_student_name= (TextView) findViewById(R.id.txt_student_name);
        txt_right_que= (TextView) findViewById(R.id.txt_right_que);
        txt_wrong_que= (TextView) findViewById(R.id.txt_wrong_que);
        txt_score= (TextView) findViewById(R.id.txt_score);
        txt_result= (TextView) findViewById(R.id.txt_result);
        btn_review= (AppCompatButton) findViewById(R.id.btn_review);
        btn_dashboard= (AppCompatButton) findViewById(R.id.btn_dashboard);


        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
         student_class= sharedPreferences.getString("student_class",null);
        student_id= sharedPreferences.getString("idtag",null);
        first_name= sharedPreferences.getString("first_name",null);
        last_name= sharedPreferences.getString("last_name",null);
        exam_id= sharedPreferences.getString("exam_id",null);
        exam_title= sharedPreferences.getString("exam_title",null);


        GetAssessmentRecord();

        txt_exam_name.setText(exam_id+" : "+exam_title);
        txt_student_name.setText(student_id+" : "+first_name+" "+ last_name);

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Assessment_Record.this, KeyReview.class);
                startActivity(i);
            }
        });
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Assessment_Record.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(exam_title);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(Assessment_Record.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void GetAssessmentRecord() { String data;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
            HttpPost httppost = new HttpPost( MyConfig.URL_GET_ASSESSMENT_RESULT);
            nameValuePairs.add(new BasicNameValuePair("exam_id", exam_id));
            nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);
            Log.e("Check Data Main", data);
            try
            {
                JSONArray json = new JSONArray(data);
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    String id = obj.getString("id");
                    String exam= obj.getString("exam");
                    String date_taken = obj.getString("date_taken");
                    String completed = obj.getString("completed");
                    String score = obj.getString("score");
                    String status = obj.getString("status");
                    String right_ans = obj.getString("right_ans");
                    String wrong_ans = obj.getString("wrong_ans");

                    Log.e("fetch data",id+"="+exam+"="+score+"="+status+"//"+wrong_ans+"//"+right_ans);


                    int right_anss = Integer.parseInt(right_ans);
                    String right_answer = String.format("%02d",right_anss);
                    txt_right_que.setText(right_answer);

                    int wrong_anss = Integer.parseInt(wrong_ans);
                    String wrong_answer = String.format("%02d",wrong_anss);
                    txt_wrong_que.setText(wrong_answer);


                    txt_score.setText(score +"%");
                    if(status.equals("You Passed!")){
                        txt_result.setTextColor(getResources().getColor(R.color.colorAccent));
                        txt_result.setText(status);
                    }else if(status.equals("You Failed!")){
                        txt_result.setTextColor(getResources().getColor(R.color.amber_800));
                        txt_result.setText(status);
                    }

                   /* sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("idtag", student_id);
                    editor.putString("first_name", first_name);
                    editor.putString("last_name", last_name);
                    editor.putString("gender", gender);
                    editor.putString("department", department);
                    editor.putString("student_class", student_class);
                    editor.putString("emailtag", email);
                    editor.putString("role", role);
                    editor.putString("avator", avator);
                    editor.commit();*/


                }

            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Assessment_Record.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
