package com.upturnoes.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upturnoes.Adapter.VideoMaterial_Adapter;
import com.upturnoes.Class.MyConfig;
import com.upturnoes.Model.VideoMaterial_Model;
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

public class Video_Material extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoMaterial_Adapter mAdapter;
    List<VideoMaterial_Model> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    AutoCompleteTextView edit_search;
    LinearLayout empty_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_video__material);

        showDialog();

        initToolbar();
        initComponent();

        empty_view  = (LinearLayout) findViewById(R.id.empty_view);
        edit_search=(AutoCompleteTextView)findViewById( R.id.et_search);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Video Material");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }
    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
       /* recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager); //

        rowListItem=new ArrayList<VideoMaterial_Model>();
        get_StudyMaterial();
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(Video_Material.this);
        progressDialog.setTitle("Video Material");
        progressDialog.setMessage("Please wait while showing Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void get_StudyMaterial(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_VEDIO_MATERIAL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getEhr(result);
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
                String sub_id= sharedPreferences.getString("sub_id",null);
                String sub_name= sharedPreferences.getString("sub_name",null);


                Log.e("sub_id",sub_id);

                params.put("sub_id",sub_id);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Video_Material.this);
        queue.add(stringRequest);
    }
    private void getEhr(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new VideoMaterial_Model(json.getString("video_id"),
                        json.getString("video_title"),
                        json.getString("video_file"),
                        json.getString("thumbnail_img"),
                        json.getString("description"),
                        json.getString("deadline"),
                        json.getString("video_type"),
                        json.getString("video_fee"),
                        json.getString("dept_name"),
                        json.getString("class_name"),
                        json.getString("subject"),
                        json.getString("video_status"),
                        json.getString("download_allowed")
                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (rowListItem.size() > 0){
            //set data and list adapter
            progressDialog.dismiss();
            mAdapter = new VideoMaterial_Adapter(this, rowListItem);
            recyclerView.setAdapter(mAdapter);

            // on item list clicked
            mAdapter.setOnItemClickListener(new VideoMaterial_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final VideoMaterial_Model obj, int position) {

                    String video_id = obj.getVideo_id();
                    String video_title = obj.getVideo_title();
                    String deadline = obj.getDeadline();
                    String video_type = obj.getVideo_type();
                    String video_fee = obj.getVideo_fee();
                    String thumbnail_img= obj.getThumbnail_img();
                    String video_file = obj.getVideo_file();
                    String description= obj.getDescription();
                    String dept_name = obj.getDept_name();
                    String class_name = obj.getClass_name();
                    String subject = obj.getSubject();
                    String video_status = obj.getVideo_status();
                    String download_allowed = obj.getDownload_allowed();


                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("video_id", video_id);
                    editor.putString("video_title", video_title);
                    editor.putString("deadline", deadline);
                    editor.putString("video_type", video_type);
                    editor.putString("video_fee", video_fee);
                    editor.putString("thumbnail_img", thumbnail_img);
                    editor.putString("video_file", video_file);
                    editor.putString("description", description);
                    editor.putString("dept_name", dept_name);
                    editor.putString("class_name", class_name);
                    editor.putString("subject", subject);
                    editor.putString("video_status", video_status);
                    editor.putString("download_allowed", download_allowed);
                    editor.commit();

                    Intent i = new Intent( Video_Material.this, View_VideoMaterial.class );
                    startActivity( i );
                    finish();
                }
            });
        }else{
            progressDialog.dismiss();
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        // Capture Text in EditText
        edit_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edit_search.getText().toString().toLowerCase( Locale.getDefault());
                mAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

}
