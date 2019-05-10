package com.upturnoes.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

public class View_VideoMaterial extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    EditText edit_enroll_num;

    TextView edit_status,txt_title,txt_subject,txt_deadline,txt_video_type,txt_video_fee,text_description;
    Button btn_check_number;
    FloatingActionButton bt_play;
    ImageView image;
    LinearLayout lyt_error,lyt_enroll_num;
    RelativeLayout lyt_play;

    String student_id,video_id,video_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_view__video_material);

        initToolbar();

        btn_check_number= (Button) findViewById(R.id.btn_check_number);
        bt_play = (FloatingActionButton) findViewById(R.id.bt_play);
        image= (ImageView) findViewById(R.id.image);
        lyt_play= (RelativeLayout) findViewById(R.id.lyt_play);
        lyt_error = (LinearLayout) findViewById(R.id.lyt_error);
        lyt_enroll_num = (LinearLayout) findViewById(R.id.lyt_enroll_num);

        edit_status = (TextView) findViewById(R.id.edit_status);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_subject = (TextView) findViewById(R.id.txt_subject);
        txt_deadline = (TextView) findViewById(R.id.txt_deadline);

        txt_video_type = (TextView) findViewById(R.id.txt_video_type);
        txt_video_fee = (TextView) findViewById(R.id.txt_video_fee);
        text_description= (TextView) findViewById(R.id.text_description);

        edit_enroll_num= (EditText) findViewById(R.id.edit_enroll_num);

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);
        video_id= sharedPreferences.getString("video_id",null);
        String video_title= sharedPreferences.getString("video_title",null);
        String deadline= sharedPreferences.getString("deadline",null);
        String video_type= sharedPreferences.getString("video_type",null);
        String video_fee= sharedPreferences.getString("video_fee",null);
        String thumbnail_img = sharedPreferences.getString("thumbnail_img",null);
        video_file= sharedPreferences.getString("video_file",null);
        String description = sharedPreferences.getString("description",null);
        String dept_name= sharedPreferences.getString("dept_name",null);
        String class_name= sharedPreferences.getString("class_name",null);
        String subject= sharedPreferences.getString("subject",null);
        String video_status= sharedPreferences.getString("video_status",null);
        String download_allowed= sharedPreferences.getString("download_allowed",null);

        Log.e("data : " ,video_file +"///"+video_status +"///"+download_allowed+"//"+video_id);

        if (video_status != null && !video_status.isEmpty() && !video_status.equals("null")){

            lyt_play.setVisibility(View.GONE);
            lyt_error.setVisibility(View.VISIBLE);

        }else {
            if(video_type.equals("Paid")){
                if(download_allowed.equals("NO")){
                    lyt_error.setVisibility(View.GONE);
                    lyt_enroll_num.setVisibility(View.VISIBLE);
                    lyt_play.setVisibility(View.GONE);
                }
            }else{
                lyt_play.setVisibility(View.VISIBLE);
                lyt_error.setVisibility(View.GONE);
            }

        }

        edit_status.setText(video_status);
        txt_title.setText(video_title);
        txt_subject.setText("Subject : "+subject);
        txt_deadline.setText("Deadline : "+deadline);
        txt_video_type.setText("Type : "+video_type);
        txt_video_fee.setText("Fee : "+video_fee);
        text_description.setText(description);

        String img_path = MyConfig.Parent_Url+"assets/thumbnail/"+thumbnail_img;
        InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
        try {
            in = (InputStream) new URL(img_path).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
        image.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
        try {
            if(in != null)
                in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Thumbnail", String.valueOf(img_path));


        btn_check_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enroll_num = edit_enroll_num.getText().toString();
                Log.e("video_id",video_id);
                if (enroll_num != null && !enroll_num.isEmpty() && !enroll_num.equals("null")) {

                    nameValuePairs.add(new BasicNameValuePair("video_id", video_id));
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

                            lyt_play.setVisibility(View.VISIBLE);
                            lyt_enroll_num.setVisibility(View.GONE);


                        } if (data.matches("Enroll number is already expired")) {
                            new android.app.AlertDialog.Builder(View_VideoMaterial.this)
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

                            new android.app.AlertDialog.Builder(View_VideoMaterial.this)
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
                    new android.app.AlertDialog.Builder(View_VideoMaterial.this)
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
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                videoIntent.setData(Uri.parse(video_file));
                startActivity(videoIntent);*/
                Intent i = new Intent(getApplicationContext(), VideoPlay.class);
                startActivity(i);
                //finish();
            }
        });
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Video Material");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Video_Material.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Video_Material.class);
        startActivity(i);
        finish();
    }

}
