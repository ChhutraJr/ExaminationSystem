package com.upturnoes.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class View_Exam extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    EditText edit_enroll_num;

    TextView edit_status,txt_exam_name,txt_subject,txt_deadline,txt_duration,txt_next_re_take,txt_passmark,txt_quetions,
                    txt_exam_type,txt_exam_fee,edit_attende_status;
    Button btn_begin_assessment,btn_check_number;
    LinearLayout lyt_error,lyt_error_attend,lyt_enroll_num;
    String student_id,exam_id;
    String terms ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            Window window = getWindow();
            WindowManager wm =getWindowManager();
            wm.removeViewImmediate(window.getDecorView());
            wm.addView(window.getDecorView(), window.getAttributes());

            new android.app.AlertDialog.Builder(View_Exam.this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("Alert !!")
                    .setMessage("Can't take screenshots !!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();
        }*/

        setContentView(R.layout.activity_view__exam);

        initToolbar();

        btn_check_number= (Button) findViewById(R.id.btn_check_number);
        btn_begin_assessment = (Button) findViewById(R.id.btn_begin_assessment);
        lyt_error = (LinearLayout) findViewById(R.id.lyt_error);
        lyt_error_attend = (LinearLayout) findViewById(R.id.lyt_error_attend);
        lyt_enroll_num = (LinearLayout) findViewById(R.id.lyt_enroll_num);

        edit_status = (TextView) findViewById(R.id.edit_status);
        txt_exam_name = (TextView) findViewById(R.id.txt_exam_name);
        txt_subject = (TextView) findViewById(R.id.txt_subject);
        txt_deadline = (TextView) findViewById(R.id.txt_deadline);
        txt_duration = (TextView) findViewById(R.id.txt_duration);
        txt_next_re_take = (TextView) findViewById(R.id.txt_next_re_take);
        txt_passmark = (TextView) findViewById(R.id.txt_passmark);
        txt_quetions = (TextView) findViewById(R.id.txt_quetions);
        txt_exam_type = (TextView) findViewById(R.id.txt_exam_type);
        txt_exam_fee = (TextView) findViewById(R.id.txt_exam_fee);
        edit_attende_status = (TextView) findViewById(R.id.edit_attende_status);
        edit_enroll_num= (EditText) findViewById(R.id.edit_enroll_num);

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);
        exam_id= sharedPreferences.getString("exam_id",null);
        String exam_title= sharedPreferences.getString("exam_title",null);
        String exam_type= sharedPreferences.getString("exam_type",null);
        String exam_fee= sharedPreferences.getString("exam_fee",null);
        String exam_deadline= sharedPreferences.getString("exam_deadline",null);
        String exam_department= sharedPreferences.getString("exam_department",null);
        String exam_class= sharedPreferences.getString("exam_class",null);
        String exam_duration= sharedPreferences.getString("exam_duration",null);
        String exam_subject= sharedPreferences.getString("exam_subject",null);
        String exam_passmark= sharedPreferences.getString("exam_passmark",null);
        String exam_retake= sharedPreferences.getString("exam_retake",null);
        String exam_status= sharedPreferences.getString("exam_status",null);
        String next_retake= sharedPreferences.getString("next_retake",null);
        String quetions= sharedPreferences.getString("quetions",null);
        String exam_attended= sharedPreferences.getString("exam_attended",null);
        String student_retake= sharedPreferences.getString("student_retake",null);
        String exam_allowed= sharedPreferences.getString("exam_allowed",null);
        String next_retake_b= sharedPreferences.getString("next_retake_b",null);//date for retake on if attended
         terms= sharedPreferences.getString("terms",null);

         Log.e("data : " ,exam_attended +"///"+exam_allowed +"///"+student_retake+"//"+next_retake_b);
       /* if (student_retake != null && !student_retake.isEmpty() && !student_retake.equals("null")){
            if (exam_allowed != null && !exam_allowed.isEmpty() && !exam_allowed.equals("null")) {

                edit_attende_status.setText("Exam Attended : " +exam_attended +" Retake : "+ student_retake +"   Exam Allowed : "+ exam_allowed);
                btn_begin_assessment.setVisibility(View.VISIBLE);
                lyt_error_attend.setVisibility(View.VISIBLE);
            }

        }else {
            edit_attende_status.setText("Exam Attended :You have already attended this exam, you will be able to re-take it again on "+ next_retake_b);
            btn_begin_assessment.setVisibility(View.GONE);
            lyt_error_attend.setVisibility(View.VISIBLE);
        }*/



        if (exam_status != null && !exam_status.isEmpty() && !exam_status.equals("null")){

            btn_begin_assessment.setVisibility(View.GONE);
            lyt_error.setVisibility(View.VISIBLE);

        }else {
            if(exam_type.equals("Paid")){
                if(exam_allowed.equals("NO")){
                    lyt_error.setVisibility(View.GONE);
                    lyt_enroll_num.setVisibility(View.VISIBLE);
                    btn_begin_assessment.setVisibility(View.GONE);
                }
            }else{
                btn_begin_assessment.setVisibility(View.VISIBLE);
                lyt_error.setVisibility(View.GONE);
            }

        }

        edit_status.setText(exam_status);
        txt_exam_name.setText(exam_title);
        txt_subject.setText(exam_subject);
        txt_deadline.setText(exam_deadline);
        txt_duration.setText(exam_duration+" Minutes");
        txt_next_re_take.setText(next_retake);
        txt_passmark.setText(exam_passmark);
        txt_quetions.setText(quetions);
        txt_exam_type.setText(exam_type);
        txt_exam_fee.setText(exam_fee);

        btn_begin_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTermServicesDialog();
            }
        });
        btn_check_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enroll_num = edit_enroll_num.getText().toString();
                if (enroll_num != null && !enroll_num.isEmpty() && !enroll_num.equals("null")) {

                            nameValuePairs.add(new BasicNameValuePair("exam_id", exam_id));
                            nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
                             nameValuePairs.add(new BasicNameValuePair("enroll_num", enroll_num));

                            try {
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost(MyConfig.URL_Check_Enroll);
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity entity = response.getEntity();
                                //  is = entity.getContent();
                                String data = EntityUtils.toString(entity);
                                Log.e("Register", data);
                                if (data.matches("Record Updated Successfully")) {
                                    Log.e("pass 1", "connection success ");

                                    btn_begin_assessment.setVisibility(View.VISIBLE);
                                    lyt_enroll_num.setVisibility(View.GONE);


                                } if (data.matches("Enroll number is already expired")) {
                                    new android.app.AlertDialog.Builder(View_Exam.this)
                                            .setIcon(R.mipmap.ic_launcher)
                                            .setTitle("Error message")
                                            .setMessage("Enroll number is Already EXPIRED !!!!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            })
                                            .show();
                                }
                                if (data.matches("Something went wrong")) {
                                    Log.e("pass 1", "connection success ");

                                    new android.app.AlertDialog.Builder(View_Exam.this)
                                            .setIcon(R.mipmap.ic_launcher)
                                            .setTitle("Error message")
                                            .setMessage("Enroll number Not Match")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            })
                                            .show();                                }

                            } catch (ClientProtocolException e) {
                                Log.e("Fail 1", e.toString());
                                Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                }else{
                    new android.app.AlertDialog.Builder(View_Exam.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Error message")
                            .setMessage("Enter your enroll number eg: EN-XXX-XXX-XXX and then Continue")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .show();
                }
            }
        });
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Assessment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Examinations.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTermServicesDialog() {
        final Dialog dialog = new Dialog(View_Exam.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_term_of_services);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ((TextView) dialog.findViewById(R.id.txt_terms)).setText(terms);
        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(View_Exam.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Begin Assessment")
                        .setMessage("Are you sure you want to begin the assessment ?")
                        .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {

                               // Intent i = new Intent(View_Exam.this, StartExam.class);
                                Intent i = new Intent(View_Exam.this, Assessment.class);
                                startActivity(i);
                                finish();
                                dialog1.dismiss();


                            }
                        })
                        .setNegativeButton(getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        })
                        .show();
                dialog.dismiss();

            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Button Decline Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Examinations.class);
        startActivity(i);
        finish();
    }
}
