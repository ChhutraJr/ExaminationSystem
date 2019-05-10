package com.upturnoes.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.upturnoes.Class.CheckForSDCard;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class View_StudyMaterial extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    EditText edit_enroll_num;

    TextView edit_status,txt_material_name,txt_subject,txt_deadline,txt_class,txt_department,txt_study_type,txt_study_fee;
    Button btn_download,btn_check_number;
    LinearLayout lyt_error,lyt_enroll_num;

    String student_id,study_id,pdf_file,allow_download;

    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = View_StudyMaterial.class.getSimpleName();
    private String url,fileurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_view__study_material);

        initToolbar();

        btn_check_number= (Button) findViewById(R.id.btn_check_number);
        btn_download = (Button) findViewById(R.id.btn_download);
        lyt_error = (LinearLayout) findViewById(R.id.lyt_error);
        lyt_enroll_num = (LinearLayout) findViewById(R.id.lyt_enroll_num);

        edit_status = (TextView) findViewById(R.id.edit_status);
        txt_material_name = (TextView) findViewById(R.id.txt_material_name);
        txt_subject = (TextView) findViewById(R.id.txt_subject);
        txt_deadline = (TextView) findViewById(R.id.txt_deadline);
        txt_class = (TextView) findViewById(R.id.txt_class);
        txt_department = (TextView) findViewById(R.id.txt_department);
        txt_study_type = (TextView) findViewById(R.id.txt_study_type);
        txt_study_fee = (TextView) findViewById(R.id.txt_study_fee);

        edit_enroll_num= (EditText) findViewById(R.id.edit_enroll_num);

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);
         study_id= sharedPreferences.getString("study_id",null);
        String study_title= sharedPreferences.getString("study_title",null);
        String deadline= sharedPreferences.getString("deadline",null);
        String study_type= sharedPreferences.getString("study_type",null);
        String study_fee= sharedPreferences.getString("study_fee",null);
        pdf_file= sharedPreferences.getString("pdf_file",null);
        allow_download= sharedPreferences.getString("allow_download",null);
        String dept_name= sharedPreferences.getString("dept_name",null);
        String class_name= sharedPreferences.getString("class_name",null);
        String subject= sharedPreferences.getString("subject",null);
        String study_status= sharedPreferences.getString("study_status",null);
        String download_allowed= sharedPreferences.getString("download_allowed",null);

        Log.e("data : " ,pdf_file +"///"+study_status +"///"+download_allowed+"//"+study_id);

        if (study_status != null && !study_status.isEmpty() && !study_status.equals("null")){

            btn_download.setVisibility(View.GONE);
            lyt_error.setVisibility(View.VISIBLE);

        }else {
            if(study_type.equals("Paid")){
                if(download_allowed.equals("NO")){
                    lyt_error.setVisibility(View.GONE);
                    lyt_enroll_num.setVisibility(View.VISIBLE);
                    btn_download.setVisibility(View.GONE);
                }
            }else{
                btn_download.setVisibility(View.VISIBLE);
                lyt_error.setVisibility(View.GONE);
            }

        }

        edit_status.setText(study_status);
        txt_material_name.setText(study_title);
        txt_subject.setText(subject);
        txt_deadline.setText(deadline);
        txt_class.setText(class_name);
        txt_department.setText(dept_name);
        txt_study_type.setText(study_type);
        txt_study_fee.setText(study_fee);

         fileurl =MyConfig.Parent_Url+"assets/upload_pdf/"+pdf_file;

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(allow_download.equalsIgnoreCase("1")) {
                        if (CheckForSDCard.isSDCardPresent()) {
                            if (ContextCompat.checkSelfPermission(View_StudyMaterial.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                //Get the URL entered
                                url = MyConfig.Parent_Url+"assets/upload_pdf/" + pdf_file;
                                new DownloadFile().execute(url);
                            } else {
                                // Request permission from the user
                                ActivityCompat.requestPermissions(View_StudyMaterial.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                            }
                        } else {

                            new android.app.AlertDialog.Builder(View_StudyMaterial.this)
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle("Download File Alert !!")
                                    .setMessage("SD Card not found, You want to Open Pdf file?")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileurl));
                                            startActivity(browserIntent);
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();


                        }
                    }else{
                        new android.app.AlertDialog.Builder(View_StudyMaterial.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("Download File Alert !!")
                                .setMessage("Download Not allow for this file, You want to Open Pdf file?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(getApplicationContext(), OpenPdf.class);
                                        startActivity(i);

                                      /*  String url= "https://docs.google.com/gview?embedded=true&url="+fileurl;
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(intent);*/

                                      /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileurl));
                                        startActivity(browserIntent);*/
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
            }
        });
        btn_check_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enroll_num = edit_enroll_num.getText().toString();
                if (enroll_num != null && !enroll_num.isEmpty() && !enroll_num.equals("null")) {

                    nameValuePairs.add(new BasicNameValuePair("study_id", study_id));
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

                            btn_download.setVisibility(View.VISIBLE);
                            lyt_enroll_num.setVisibility(View.GONE);


                        } if (data.matches("Enroll number is already expired")) {
                            new android.app.AlertDialog.Builder(View_StudyMaterial.this)
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

                            new android.app.AlertDialog.Builder(View_StudyMaterial.this)
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
                    new android.app.AlertDialog.Builder(View_StudyMaterial.this)
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
        getSupportActionBar().setTitle("View Study Material");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Study_Material.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Study_Material.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                url = MyConfig.Parent_Url+"assets/upload_pdf/"+pdf_file;
                new DownloadFile().execute(url);
        }
    }
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, View_StudyMaterial.this);
    }*/

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        //Download the file once permission is granted
        url = MyConfig.Parent_Url+"assets/upload_pdf/"+pdf_file;
        Log.e("url",url);
        new DownloadFile().execute(url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(View_StudyMaterial.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "ExamPdf/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileurl));
                startActivity(browserIntent);
                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
}
