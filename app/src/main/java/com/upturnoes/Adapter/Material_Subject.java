package com.upturnoes.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.upturnoes.Activity.Study_Material;
import com.upturnoes.Activity.Subject_List;
import com.upturnoes.Activity.Video_Material;
import com.upturnoes.Class.MyConfig;
import com.upturnoes.MainActivity;
import com.upturnoes.Model.Notice_Model_List;
import com.upturnoes.Model.Subject_Model_List;
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

public class Material_Subject extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MaterialSubject_Adapter mAdapter;
    List<Subject_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AutoCompleteTextView edit_search;
    LinearLayout empty_view;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material__subject);


        initToolbar();
        showDialog();

        empty_view  = (LinearLayout) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        edit_search=(AutoCompleteTextView)findViewById( R.id.et_search);
        rowListItem=new ArrayList<Subject_Model_List>();
        get_SUbjects();


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subjects");
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
    private void showDialog() {
        progressDialog = new ProgressDialog(Material_Subject.this);
        progressDialog.setTitle("Subjects");
        progressDialog.setMessage("Please wait while showing Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void get_SUbjects(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_SUBJECTS,
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

                Log.e("student_class",student_class);

                params.put("student_class",student_class);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Material_Subject.this);
        queue.add(stringRequest);
    }
    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Subject_Model_List(json.getString("sub_id"),
                        json.getString("sub_name"),
                        json.getString("dept_name"),
                        json.getString("class_name"),
                        json.getString("reg_date")

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0){
            progressDialog.dismiss();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            //rowListItem = get_ALL_CATEGORY();
            mAdapter =new MaterialSubject_Adapter(Material_Subject.this,rowListItem);
            mRecyclerView.setAdapter(mAdapter);

            // on item list clicked
            mAdapter.setOnItemClickListener(new MaterialSubject_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Subject_Model_List obj, int position) {

                    showDialogImageQuotes(obj);

                }
            });

            mAdapter.notifyDataSetChanged();

        }else{
            progressDialog.dismiss();
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
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

    private void showDialogImageQuotes(final Subject_Model_List obj) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_study_type);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        ((TextView) dialog.findViewById(R.id.pdf_files)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub_id = obj.getSub_id();
                String sub_name = obj.getSub_id();

                sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("sub_id", sub_id);
                editor.putString("sub_name", sub_name);
                editor.commit();

                Intent i = new Intent(Material_Subject.this, Study_Material.class);
                startActivity(i);
                dialog.dismiss();

            }
        });

        ((TextView) dialog.findViewById(R.id.vedio_files)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub_id = obj.getSub_id();
                String sub_name = obj.getSub_id();

                sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("sub_id", sub_id);
                editor.putString("sub_name", sub_name);
                editor.commit();

                Intent i = new Intent(Material_Subject.this, Video_Material.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
