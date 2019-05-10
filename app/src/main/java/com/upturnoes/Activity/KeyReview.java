package com.upturnoes.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upturnoes.Adapter.Review_Adapter;
import com.upturnoes.Class.MyConfig;
import com.upturnoes.Model.Review_Model;
import com.upturnoes.R;
import com.upturnoes.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyReview extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Review_Adapter mAdapter;
    List<Review_Model> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_review);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        rowListItem=new ArrayList<Review_Model>();
        get_REVIEW();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review");
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

    public void get_REVIEW(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_REVIEW,
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
                String student_id= sharedPreferences.getString("idtag",null);
                String exam_id= sharedPreferences.getString("exam_id",null);

                Log.e("exam_id",exam_id);

                params.put("student_id",student_id);
                params.put("exam_id",exam_id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(KeyReview.this);
        queue.add(stringRequest);
    }
    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Review_Model(json.getString("quetion_id"),
                        json.getString("selected_id"),
                        json.getString("selected_answer"),
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
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            mAdapter =new Review_Adapter(KeyReview.this,rowListItem);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }



    }

}
