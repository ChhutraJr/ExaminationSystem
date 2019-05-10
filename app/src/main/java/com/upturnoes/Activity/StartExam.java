package com.upturnoes.Activity;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.upturnoes.Adapter.Quetion_Adapter;
import com.upturnoes.Class.MyConfig;

import com.upturnoes.Model.Quetion_Model_List;
import com.upturnoes.R;
import com.upturnoes.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartExam extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Quetion_Adapter mAdapter;
    List<Quetion_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

   TextView txt_timer;
    LinearLayout empty_view;
    String exam_id,exam_title,exam_duration;

    Button btn_Previous,btn_Next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exam);

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
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
        String student_retake= sharedPreferences.getString("student_retake",null);
        String exam_allowed= sharedPreferences.getString("exam_allowed",null);
        String next_retake_b= sharedPreferences.getString("next_retake_b",null);

        initToolbar();

        empty_view  = (LinearLayout) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.HORIZONTAL,false);
        SnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        snapHelper.attachToRecyclerView(mRecyclerView);

        btn_Previous = (Button) findViewById(R.id.btn_Previous);
        btn_Next = (Button) findViewById(R.id.btn_Next);
       // btn_Previous.setVisibility(View.GONE);
         txt_timer=(TextView)findViewById( R.id.txt_timer);
        //txt_timer.setText(exam_duration +"Minutes");
                int minutes = Integer.parseInt(exam_duration);
       

         new CountDownTimer(60*minutes*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_timer.setText(hms);//set text
            }
            public void onFinish() {
                txt_timer.setText("TIME'S UP!!"); //On finish change timer text
            }
        }.start();




        rowListItem=new ArrayList<Quetion_Model_List>();
        get_Questions();


        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.getLayoutManager().scrollToPosition(gridLayoutManager.findFirstVisibleItemPosition() - 1);
              //  btn_Next.setVisibility(View.VISIBLE);
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.getLayoutManager().scrollToPosition(gridLayoutManager.findLastVisibleItemPosition() + 1);
               // btn_Previous.setVisibility(View.VISIBLE);
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
            finish();
        }

        return super.onOptionsItemSelected(item);
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

                Log.e("exam_id",exam_id);

                params.put("exam_id",exam_id);


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(StartExam.this);
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

            mAdapter =new Quetion_Adapter(StartExam.this,rowListItem);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else{
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }



    }




}
