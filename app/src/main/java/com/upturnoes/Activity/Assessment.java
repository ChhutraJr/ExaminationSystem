package com.upturnoes.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upturnoes.Adapter.Quetion_Adapter;
import com.upturnoes.Class.MyConfig;
import com.upturnoes.Class.URLImageParser;
import com.upturnoes.MainActivity;
import com.upturnoes.Model.Quetion_Model_List;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Assessment extends AppCompatActivity {

    List<Quetion_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText edit_fill_answer;
    public TextView txt_quetion;
    RadioGroup radiogrp;
    RadioButton radio1,radio2,radio3,radio4;

    TextView txt_timer;
    LinearLayout empty_view;
    String student_id,exam_id,exam_title,exam_duration;

    Button btn_Previous,btn_Next,btn_submit;
    int i=0;
    String realaAns,student_retake;

    ProgressDialog progressDialog;
    private boolean isCanceled = false;

    AppCompatCheckBox check_invalid;
    int count;

    ImageView image_quetion,image_op1,image_op2,image_op3,image_op4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_assessment);

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String student_class= sharedPreferences.getString("student_class",null);
         student_id= sharedPreferences.getString("idtag",null);
        exam_id= sharedPreferences.getString("exam_id",null);
        exam_title= sharedPreferences.getString("exam_title",null);
        String exam_type= sharedPreferences.getString("exam_type",null);
        String exam_fee= sharedPreferences.getString("exam_fee",null);
        String exam_deadline= sharedPreferences.getString("exam_deadline",null);
        String exam_department= sharedPreferences.getString("exam_department",null);
        String exam_class= sharedPreferences.getString("exam_class",null);
        exam_duration= sharedPreferences.getString("exam_duration",null);
        String exam_subject= sharedPreferences.getString("exam_subject",null);
        String exam_passmark= sharedPreferences.getString("exam_passmark",null);
        String exam_retake= sharedPreferences.getString("exam_retake",null);
        String exam_status= sharedPreferences.getString("exam_status",null);
        String next_retake= sharedPreferences.getString("next_retake",null);
        String quetions= sharedPreferences.getString("quetions",null);
        String exam_attended= sharedPreferences.getString("exam_attended",null);
         student_retake= sharedPreferences.getString("student_retake",null);
        String exam_allowed= sharedPreferences.getString("exam_allowed",null);
        String next_retake_b= sharedPreferences.getString("next_retake_b",null);

        initToolbar();
        deleteCache(Assessment.this);
        showDialog();

        txt_quetion = (TextView) findViewById(R.id.txt_quetion);
        radiogrp = (RadioGroup) findViewById(R.id.radiogrp);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        edit_fill_answer= (EditText) findViewById(R.id.edit_fill_answer);

        image_quetion = (ImageView) findViewById(R.id.image_quetion);
        image_op1 = (ImageView) findViewById(R.id.image_op1);
        image_op2 = (ImageView) findViewById(R.id.image_op2);
        image_op3 = (ImageView) findViewById(R.id.image_op3);
        image_op4 = (ImageView) findViewById(R.id.image_op4);


        btn_submit= (Button) findViewById(R.id.btn_submit);
        btn_Previous = (Button) findViewById(R.id.btn_Previous);
        btn_Next = (Button) findViewById(R.id.btn_Next);
        txt_timer=(TextView)findViewById( R.id.txt_timer);
        int minutes = Integer.parseInt(exam_duration);

        check_invalid = (AppCompatCheckBox) findViewById(R.id.check_invalid);

        Submit_Assessment();

        new CountDownTimer(60*minutes*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(isCanceled)
                {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                }else {
                    long millis = millisUntilFinished;
                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    txt_timer.setText(hms);//set text
                }
            }
            public void onFinish() {
                txt_timer.setText("TIME'S UP!!");
                Update_Assessment(); //On finish change timer text
            }
        }.start();


        rowListItem=new ArrayList<Quetion_Model_List>();
        get_Questions();

        if(i==0){
            btn_Previous.setVisibility(View.GONE);
        }else{
            btn_Previous.setVisibility(View.VISIBLE);
        }


        check_invalid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    String quetion_id = rowListItem.get(i).getQuetion_id();
                    nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
                    nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
                    nameValuePairs.add( new BasicNameValuePair( "quetion_id", quetion_id ) );

                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost( MyConfig.URL_ADD_INVALID_QUETION );
                        httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
                        HttpResponse response = httpclient.execute( httppost );
                        HttpEntity entity = response.getEntity();
                        //  is = entity.getContent();
                        String data = EntityUtils.toString( entity ).trim();
                        //  Log.e( "Register", data );
                        if (data.matches( "success" )) {
                            // Log.e( "pass 1", "Select ans : "+ s.toString()+" Real ans : "+ real_ans );
                        }
                        if (data.matches( "failure" )) {
                            //Log.e( "pass 1", "connection success " );
                        }
                    } catch (ClientProtocolException e) {
                        //Log.e( "Fail 1", e.toString() );
                        Toast.makeText( getApplicationContext(), "Invalid IP", Toast.LENGTH_LONG ).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //textview.setText(s.toString());
                String quetion_id = rowListItem.get(i).getQuetion_id();
                String real_ans = rowListItem.get(i).getAnswer();
                nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
                nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
                nameValuePairs.add( new BasicNameValuePair( "quetion_id", quetion_id ) );
                nameValuePairs.add( new BasicNameValuePair( "checked_id","6"));//6 added as a random id for filling the blank
                nameValuePairs.add( new BasicNameValuePair( "selected_answer", s.toString() ) );
                nameValuePairs.add( new BasicNameValuePair( "real_ans", real_ans ) );
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost( MyConfig.URL_ADD_ANSWERD_QUETION );
                    httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
                    HttpResponse response = httpclient.execute( httppost );
                    HttpEntity entity = response.getEntity();
                    //  is = entity.getContent();
                    String data = EntityUtils.toString( entity ).trim();
                  //  Log.e( "Register", data );
                    if (data.matches( "success" )) {
                       // Log.e( "pass 1", "Select ans : "+ s.toString()+" Real ans : "+ real_ans );
                    }
                    if (data.matches( "failure" )) {
                        //Log.e( "pass 1", "connection success " );
                    }
                } catch (ClientProtocolException e) {
                    //Log.e( "Fail 1", e.toString() );
                    Toast.makeText( getApplicationContext(), "Invalid IP", Toast.LENGTH_LONG ).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        edit_fill_answer.addTextChangedListener(inputTextWatcher);

        radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                final String selected_answer;
                int radioid;
                RadioButton rb= (RadioButton) findViewById(checkedId);
                int selectedId = radiogrp.getCheckedRadioButtonId();
               // Log.e("ID", String.valueOf(selectedId));
                if (checkedId == R.id.radio1) {
                    radioid = 1;
                    selected_answer = "op1";
                }
                else if (checkedId == R.id.radio2){
                    radioid = 2;
                    selected_answer = "op2";
                }
                else if (checkedId == R.id.radio3) {

                    radioid = 3;
                    selected_answer = "op3";

                } else if (checkedId == R.id.radio4) {

                    radioid = 4;
                    selected_answer = "op4";
                }else{
                    radioid=0;
                    selected_answer="";
                }
                //selected_answer = rb.getText().toString();
                String quetion_id = rowListItem.get(i).getQuetion_id();
                String real_ans = rowListItem.get(i).getAnswer();

                nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
                nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
                nameValuePairs.add( new BasicNameValuePair( "quetion_id", quetion_id ) );
                nameValuePairs.add( new BasicNameValuePair( "checked_id",String.valueOf(radioid) ) );
                nameValuePairs.add( new BasicNameValuePair( "selected_answer", selected_answer ) );
                nameValuePairs.add( new BasicNameValuePair( "real_ans", real_ans ) );
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost( MyConfig.URL_ADD_ANSWERD_QUETION );
                    httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
                    HttpResponse response = httpclient.execute( httppost );
                    HttpEntity entity = response.getEntity();
                    //  is = entity.getContent();
                    String data = EntityUtils.toString( entity ).trim();
                   // Log.e( "Register", data );
                    if (data.matches( "success" )) {
                       // Log.e( "pass 1", "Select ans : "+ selected_answer+" Real ans : "+ real_ans );
                    }
                    if (data.matches( "failure" )) {
                       // Log.e( "pass 1", "connection success " );
                    }
                } catch (ClientProtocolException e) {
                   // Log.e( "Fail 1", e.toString() );
                    Toast.makeText( getApplicationContext(), "Invalid IP", Toast.LENGTH_LONG ).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_Next.setVisibility(View.VISIBLE);
                i--;
                //Log.e("student_id data",student_id+"//"+exam_id+"///"+i);
                GetInvalidMarked();
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
                    HttpPost httppost = new HttpPost( MyConfig.URL_selected_answer);
                    nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
                    nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
                    nameValuePairs.add( new BasicNameValuePair( "quetion_id", rowListItem.get(i).getQuetion_id() ) );
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity).trim();
                    Log.e("data",data);
                    Log.e("student_id",student_id+"///"+exam_id+"//"+rowListItem.get(i).getQuetion_id());
                    try
                    {
                        if (data != null && !data.isEmpty() && !data.equals("null")){

                            JSONArray json = new JSONArray(data);
                            for (int i = 0; i < json.length(); i++)
                            {
                                JSONObject obj = json.getJSONObject(i);

                                int selected_id = obj.getInt("selected_id");
                                String selected_answer = obj.getString("selected_answer");

                                if (selected_answer != null && !selected_answer.isEmpty() && !selected_answer.equals("null")){
                                    if(selected_id==1){
                                        radio1.setChecked(true);
                                    }else if(selected_id==2){
                                        radio2.setChecked(true);
                                    }else if(selected_id==3){
                                        radio3.setChecked(true);
                                    }else if(selected_id==4){
                                        radio4.setChecked(true);
                                    }else{
                                        // edit_fill_answer.setVisibility(View.VISIBLE);
                                        //  radiogrp.setVisibility(View.GONE);
                                        edit_fill_answer.setText(selected_answer);
                                    }

                                }else{
                                    edit_fill_answer.setText(null);
                                    radiogrp.clearCheck();
                                }

                               // Log.e("selected_answer data",selected_id+"//"+selected_answer);

                            }

                        }else{
                            edit_fill_answer.setText(null);
                            radiogrp.clearCheck();
                        }

                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (ClientProtocolException e) {
                   // Log.e("Fail 1", e.toString());
                    Toast.makeText(getApplicationContext(),"Invalid IP", Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                int srno = i+1;
               // Log.e("size", String.valueOf(rowListItem.size()) +" i = "+i +"srno:"+srno);

                if(i==0){
                    btn_Previous.setVisibility(View.GONE);
                    btn_Next.setVisibility(View.VISIBLE);
                }

                   // txt_quetion.setText(srno+".  "+rowListItem.get(i).getQuestion());
                txt_quetion.setText(HtmlCompat.fromHtml(srno+".  "+rowListItem.get(i).getQuestion(), 0));

                    if(rowListItem.get(i).getOp1().equals("-")  && rowListItem.get(i).getOp1().equals("-")){
                        edit_fill_answer.setVisibility(View.VISIBLE);
                        radiogrp.setVisibility(View.GONE);
                    }else{
                        edit_fill_answer.setVisibility(View.GONE);
                        radiogrp.setVisibility(View.VISIBLE);
                       /* radio1.setText(rowListItem.get(i).getOp1());
                        radio2.setText(rowListItem.get(i).getOp2());
                        radio3.setText(rowListItem.get(i).getOp3());
                        radio4.setText(rowListItem.get(i).getOp4());*/
                        radio1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp1(), 0));
                        radio2.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp2(), 0));
                        radio3.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp3(), 0));
                        radio4.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp4(), 0));


                        if (rowListItem.get(i).getQuestion_image() != null && !rowListItem.get(i).getQuestion_image().isEmpty() && !rowListItem.get(i).getQuestion_image().equals("null")){

                            image_quetion.setVisibility(View.VISIBLE);
                            String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getQuestion_image();
                            InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                            try {
                                in = (InputStream) new URL(img_path).getContent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                            image_quetion.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                            try {
                                if(in != null)
                                    in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_quetion.setVisibility(View.GONE);
                        }

                        if (rowListItem.get(i).getOp1_image() != null && !rowListItem.get(i).getOp1_image().isEmpty() && !rowListItem.get(i).getOp1_image().equals("null")){

                            image_op1.setVisibility(View.VISIBLE);
                            String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp1_image();
                            InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                            try {
                                in = (InputStream) new URL(img_path).getContent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                            image_op1.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                            try {
                                if(in != null)
                                    in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_op1.setVisibility(View.GONE);
                        }

                        if (rowListItem.get(i).getOp2_image() != null && !rowListItem.get(i).getOp2_image().isEmpty() && !rowListItem.get(i).getOp2_image().equals("null")){

                            image_op2.setVisibility(View.VISIBLE);
                            String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp2_image();
                            InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                            try {
                                in = (InputStream) new URL(img_path).getContent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                            image_op2.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                            try {
                                if(in != null)
                                    in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_op2.setVisibility(View.GONE);
                        }


                        if (rowListItem.get(i).getOp3_image() != null && !rowListItem.get(i).getOp3_image().isEmpty() && !rowListItem.get(i).getOp3_image().equals("null")){

                            image_op3.setVisibility(View.VISIBLE);
                            String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp3_image();
                            InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                            try {
                                in = (InputStream) new URL(img_path).getContent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                            image_op3.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                            try {
                                if(in != null)
                                    in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_op3.setVisibility(View.GONE);
                        }


                        if (rowListItem.get(i).getOp4_image() != null && !rowListItem.get(i).getOp4_image().isEmpty() && !rowListItem.get(i).getOp4_image().equals("null")){

                            image_op4.setVisibility(View.VISIBLE);
                            String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp4_image();
                            InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                            try {
                                in = (InputStream) new URL(img_path).getContent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                            image_op4.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                            try {
                                if(in != null)
                                    in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_op4.setVisibility(View.GONE);
                        }


                    }
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
              //  Log.e("student_id data",student_id+"//"+exam_id+"///"+i);

                GetInvalidMarked();
              try {
                    HttpClient httpclient = new DefaultHttpClient();
                    //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
                    HttpPost httppost = new HttpPost( MyConfig.URL_selected_answer);
                    nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
                    nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
                    nameValuePairs.add( new BasicNameValuePair( "quetion_id", rowListItem.get(i).getQuetion_id() ) );
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity).trim();
                    Log.e("data",data);
                  Log.e("student_id",student_id+"///"+exam_id+"//"+rowListItem.get(i).getQuetion_id());
                    try
                    {
                        if (data != null && !data.isEmpty() && !data.equals("null")){

                            JSONArray json = new JSONArray(data);
                            for (int i = 0; i < json.length(); i++)
                            {
                                JSONObject obj = json.getJSONObject(i);

                                int selected_id = obj.getInt("selected_id");
                                String selected_answer = obj.getString("selected_answer");


                                if (selected_answer != null && !selected_answer.isEmpty() && !selected_answer.equals("null")){
                                    if(selected_id==1){
                                        radio1.setChecked(true);
                                    }else if(selected_id==2){
                                        radio2.setChecked(true);
                                    }else if(selected_id==3){
                                        radio3.setChecked(true);
                                    }else if(selected_id==4){
                                        radio4.setChecked(true);
                                    }else{
                                       // edit_fill_answer.setVisibility(View.VISIBLE);
                                      //  radiogrp.setVisibility(View.GONE);
                                        edit_fill_answer.setText(selected_answer);
                                    }

                                }else{
                                    radiogrp.clearCheck();
                                    edit_fill_answer.setText(null);
                                }

                              //  Log.e("selected_answer data",selected_id+"//"+selected_answer);

                            }

                        }else{
                            radiogrp.clearCheck();
                            edit_fill_answer.setText(null);
                        }

                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (ClientProtocolException e) {
                   // Log.e("Fail 1", e.toString());
                    Toast.makeText(getApplicationContext(),"Invalid IP", Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                btn_Previous.setVisibility(View.VISIBLE);


                if(i<rowListItem.size()){
                    int srno = i+1;
                    //Log.e("size", String.valueOf(rowListItem.size()) +" i = "+i +"srno:"+srno);

                    if(rowListItem.size()==i+1){
                        btn_Next.setVisibility(View.GONE);
                        btn_Previous.setVisibility(View.VISIBLE);
                    }
                       // txt_quetion.setText(srno+".  "+rowListItem.get(i).getQuestion());
                    txt_quetion.setText(HtmlCompat.fromHtml(srno+".  "+rowListItem.get(i).getQuestion(), 0));

                        if(rowListItem.get(i).getOp1().equals("-")  && rowListItem.get(i).getOp1().equals("-")){
                            edit_fill_answer.setVisibility(View.VISIBLE);
                            radiogrp.setVisibility(View.GONE);
                        }else{
                            edit_fill_answer.setVisibility(View.GONE);
                            radiogrp.setVisibility(View.VISIBLE);
                          /*  radio1.setText(rowListItem.get(i).getOp1());
                            radio2.setText(rowListItem.get(i).getOp2());
                            radio3.setText(rowListItem.get(i).getOp3());
                            radio4.setText(rowListItem.get(i).getOp4());*/
                            radio1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp1(), 0));
                            radio2.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp2(), 0));
                            radio3.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp3(), 0));
                            radio4.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp4(), 0));


                            if (rowListItem.get(i).getQuestion_image() != null && !rowListItem.get(i).getQuestion_image().isEmpty() && !rowListItem.get(i).getQuestion_image().equals("null")){

                                image_quetion.setVisibility(View.VISIBLE);
                                String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getQuestion_image();
                                InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                try {
                                    in = (InputStream) new URL(img_path).getContent();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                image_quetion.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                                try {
                                    if(in != null)
                                        in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("quetion img", String.valueOf(img_path));

                            }else{
                                image_quetion.setVisibility(View.GONE);
                            }

                            if (rowListItem.get(i).getOp1_image() != null && !rowListItem.get(i).getOp1_image().isEmpty() && !rowListItem.get(i).getOp1_image().equals("null")){

                                image_op1.setVisibility(View.VISIBLE);
                                String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp1_image();
                                InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                try {
                                    in = (InputStream) new URL(img_path).getContent();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                image_op1.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                                try {
                                    if(in != null)
                                        in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("quetion img", String.valueOf(img_path));

                            }else{
                                image_op1.setVisibility(View.GONE);
                            }

                            if (rowListItem.get(i).getOp2_image() != null && !rowListItem.get(i).getOp2_image().isEmpty() && !rowListItem.get(i).getOp2_image().equals("null")){

                                image_op2.setVisibility(View.VISIBLE);
                                String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp2_image();
                                InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                try {
                                    in = (InputStream) new URL(img_path).getContent();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                image_op2.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                                try {
                                    if(in != null)
                                        in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("quetion img", String.valueOf(img_path));

                            }else{
                                image_op2.setVisibility(View.GONE);
                            }


                            if (rowListItem.get(i).getOp3_image() != null && !rowListItem.get(i).getOp3_image().isEmpty() && !rowListItem.get(i).getOp3_image().equals("null")){

                                image_op3.setVisibility(View.VISIBLE);
                                String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp3_image();
                                InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                try {
                                    in = (InputStream) new URL(img_path).getContent();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                image_op3.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                                try {
                                    if(in != null)
                                        in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("quetion img", String.valueOf(img_path));

                            }else{
                                image_op3.setVisibility(View.GONE);
                            }


                            if (rowListItem.get(i).getOp4_image() != null && !rowListItem.get(i).getOp4_image().isEmpty() && !rowListItem.get(i).getOp4_image().equals("null")){

                                image_op4.setVisibility(View.VISIBLE);
                                String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp4_image();
                                InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                                try {
                                    in = (InputStream) new URL(img_path).getContent();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                                image_op4.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                                try {
                                    if(in != null)
                                        in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("quetion img", String.valueOf(img_path));

                            }else{
                                image_op4.setVisibility(View.GONE);
                            }

                        }


                }


                //i++;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(Assessment.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Submit Exam")
                        .setMessage("Do you really want to Submit the Exam ?")
                        .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isCanceled = true;
                                Update_Assessment();
                            }
                        })
                        .setNegativeButton(getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(exam_title);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(Assessment.this);
        progressDialog.setTitle("Assessment");
        progressDialog.setMessage("Please wait while showing Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }
   public void get_Questions(){
       StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_QUETIONS,
               new com.android.volley.Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       JSONObject j = null;
                       try {
                           j = new JSONObject(response);
                           result = j.getJSONArray(JSON_ARRAY);
                           getCategory(result);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new com.android.volley.Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {

                   }

               })
       {
           @Override
           protected Map<String, String> getParams() {
               Map<String, String> params = new HashMap<String, String>();


               sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
               sharedPreferences.edit();
               String student_class= sharedPreferences.getString("student_class",null);
               String student_id= sharedPreferences.getString("idtag",null);
               String exam_id= sharedPreferences.getString("exam_id",null);

              // Log.e("exam_id",exam_id);

               params.put("exam_id",exam_id);


               return params;
           }
       };
       RequestQueue queue = Volley.newRequestQueue(Assessment.this);
       queue.add(stringRequest);
   }
    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Quetion_Model_List(json.getString("quetion_id"),
                        json.getString("question"),
                        json.getString("type"),
                        json.getString("op1"),
                        json.getString("op2"),
                        json.getString("op3"),
                        json.getString("op4"),
                        json.getString("answer"),
                        json.getString("question_image"),
                        json.getString("op1_image"),
                        json.getString("op2_image"),
                        json.getString("op3_image"),
                        json.getString("op4_image")

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0){
            progressDialog.dismiss();
            int srno = i+1;
            //txt_quetion.setText(srno+".  "+rowListItem.get(i).getQuestion());
            //txt_quetion.setText(Html.fromHtml(rowListItem.get(i).getQuestion(), new URLImageParser(txt_quetion, this), null));
            String htmlText =rowListItem.get(i).getQuestion();
            txt_quetion.setText(HtmlCompat.fromHtml(htmlText, 0));

            if(rowListItem.get(i).getOp1().equals("-")  && rowListItem.get(i).getOp1().equals("-")){
                edit_fill_answer.setVisibility(View.VISIBLE);
                radiogrp.setVisibility(View.GONE);
            }else{
                edit_fill_answer.setVisibility(View.GONE);
                radiogrp.setVisibility(View.VISIBLE);
              //  radio1.setText(rowListItem.get(i).getOp1());
             //   radio2.setText(rowListItem.get(i).getOp2());
              //  radio3.setText(rowListItem.get(i).getOp3());
               // radio4.setText(rowListItem.get(i).getOp4());

                radio1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp1(), 0));
                radio2.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp2(), 0));
                radio3.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp3(), 0));
                radio4.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp4(), 0));

                if (rowListItem.get(i).getQuestion_image() != null && !rowListItem.get(i).getQuestion_image().isEmpty() && !rowListItem.get(i).getQuestion_image().equals("null")){

                    image_quetion.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getQuestion_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    image_quetion.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_quetion.setVisibility(View.GONE);
                }

                if (rowListItem.get(i).getOp1_image() != null && !rowListItem.get(i).getOp1_image().isEmpty() && !rowListItem.get(i).getOp1_image().equals("null")){

                    image_op1.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp1_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    image_op1.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_op1.setVisibility(View.GONE);
                }

                if (rowListItem.get(i).getOp2_image() != null && !rowListItem.get(i).getOp2_image().isEmpty() && !rowListItem.get(i).getOp2_image().equals("null")){

                    image_op2.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp2_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    image_op2.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_op2.setVisibility(View.GONE);
                }


                if (rowListItem.get(i).getOp3_image() != null && !rowListItem.get(i).getOp3_image().isEmpty() && !rowListItem.get(i).getOp3_image().equals("null")){

                    image_op3.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp3_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    image_op3.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_op3.setVisibility(View.GONE);
                }


                if (rowListItem.get(i).getOp4_image() != null && !rowListItem.get(i).getOp4_image().isEmpty() && !rowListItem.get(i).getOp4_image().equals("null")){

                    image_op4.setVisibility(View.VISIBLE);
                    String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+rowListItem.get(i).getOp4_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    image_op4.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_op4.setVisibility(View.GONE);
                }

            }
        }else{
            progressDialog.dismiss();
            empty_view.setVisibility(View.VISIBLE);

        }


    }

    public void Submit_Assessment(){

        nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
        nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
        nameValuePairs.add( new BasicNameValuePair( "student_retake", student_retake ) );

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_ADD_ASSESSMENT );
            httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
            HttpResponse response = httpclient.execute( httppost );
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            String data = EntityUtils.toString( entity ).trim();
            //Log.e( "Register", data );
            if (data.matches( "success" )) {
               // Log.e( "pass 1", "Select ans : "+ selected_answer+" Real ans : "+ real_ans );
            }
            if (data.matches( "failure" )) {
               // Log.e( "pass 1", "connection success " );
            }
        } catch (ClientProtocolException e) {
           // Log.e( "Fail 1", e.toString() );
            Toast.makeText( getApplicationContext(), "Invalid IP", Toast.LENGTH_LONG ).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Update_Assessment(){

        nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
        nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
        nameValuePairs.add( new BasicNameValuePair( "student_retake", student_retake ) );

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( MyConfig.URL_UPDATE_ASSESSMENT );
            httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
            HttpResponse response = httpclient.execute( httppost );
            HttpEntity entity = response.getEntity();
            //  is = entity.getContent();
            String data = EntityUtils.toString( entity ).trim();
            //Log.e( "Register", data );
            if (data.matches( "success" )) {
                isCanceled = true;
                Intent i = new Intent(Assessment.this, Assessment_Record.class);
                startActivity(i);
                finish();
            }
            if (data.matches( "failure" )) {
               // Log.e( "pass 1", "connection success " );
            }
        } catch (ClientProtocolException e) {
            //Log.e( "Fail 1", e.toString() );
            Toast.makeText( getApplicationContext(), "Invalid IP", Toast.LENGTH_LONG ).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    private void GetInvalidMarked() { String data1;

        try {

            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
            HttpPost httppost = new HttpPost( MyConfig.URL_INVALID_GET);
            nameValuePairs.add( new BasicNameValuePair( "student_id", student_id ) );
            nameValuePairs.add( new BasicNameValuePair( "exam_id", exam_id ) );
            nameValuePairs.add( new BasicNameValuePair( "quetion_id", rowListItem.get(i).getQuetion_id() ) );
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            data1 = EntityUtils.toString(entity);
            Log.e("Check Data Main", data1);
            try
            {
                JSONArray json = new JSONArray(data1);
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    count = obj.getInt("count");

                    Log.e("invalid data", String.valueOf(count));

                                if(count == 1){
                                    check_invalid.setChecked(true);
                                    Log.e("Invalid 1", String.valueOf(count));
                                }else{
                                    check_invalid.setChecked(false);
                                    Log.e("Invalid 2", String.valueOf(count));
                                }
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
        Intent i = new Intent(Assessment.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
