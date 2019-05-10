package com.upturnoes;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.text.HtmlCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.upturnoes.Activity.Assessment;
import com.upturnoes.Activity.AssessmentRecord;
import com.upturnoes.Activity.Change_Password;
import com.upturnoes.Activity.DemoEnquiry;
import com.upturnoes.Activity.EditProfile;
import com.upturnoes.Activity.Examinations;
import com.upturnoes.Activity.Help;
import com.upturnoes.Activity.Login;
import com.upturnoes.Activity.MyEnroll;
import com.upturnoes.Activity.NoItemInternetImage;
import com.upturnoes.Activity.Notice;
import com.upturnoes.Activity.Study_Material;
import com.upturnoes.Activity.Subject_List;
import com.upturnoes.Activity.VideoPlay;
import com.upturnoes.Adapter.Material_Subject;
import com.upturnoes.Class.MyConfig;
import com.upturnoes.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

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
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.Subject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private ActionBar actionBar;
    private Toolbar toolbar;
    String student_id,first_name,last_name,gender,department,student_class,email,role,avator,dept_name,class_name;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    TextView txt_username,txt_class_dept;
    CircularImageView  Header_img;

    SliderLayout sliderLayout ;
    HashMap<String, Integer> HashMapForLocalRes ;

    LinearLayout lyt_examination,lyt_notice,lyt_assessment,lyt_subjects,lyt_download,lyt_alerts;
    Button btn_demo_enquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        deleteCache(MainActivity.this);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {
                // connected to wif

            }
        } else {

            Intent i = new Intent(MainActivity.this, NoItemInternetImage.class);
            startActivity(i);
        }

        GetUserdata();


        initToolbar();
        initNavigationMenu();

        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        AddImageUrlFormLocalRes();
        for(String name : HashMapForLocalRes.keySet()){

            TextSliderView textSliderView = new TextSliderView(MainActivity.this);

            textSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
                    .setScaleType( BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(MainActivity.this);

        showCustomDialog();

        lyt_examination = (LinearLayout) findViewById(R.id.lyt_examination);
        lyt_notice = (LinearLayout) findViewById(R.id.lyt_notice);
        lyt_assessment = (LinearLayout) findViewById(R.id.lyt_assessment);
        lyt_subjects = (LinearLayout) findViewById(R.id.lyt_subjects);
        lyt_download = (LinearLayout) findViewById(R.id.lyt_download);
        lyt_alerts = (LinearLayout) findViewById(R.id.lyt_alerts);

        lyt_examination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Examinations.class);
                startActivity(i);
            }
        });
        lyt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Notice.class);
                startActivity(i);
            }
        });
        lyt_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AssessmentRecord.class);
                startActivity(i);
            }
        });
        lyt_subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Subject_List.class);
                startActivity(i);
            }
        });
        lyt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Material_Subject.class);
                startActivity(i);
            }
        });
        lyt_alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Notice.class);
                startActivity(i);
            }
        });

        btn_demo_enquiry = (Button) findViewById(R.id.btn_demo_enquiry);
        btn_demo_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DemoEnquiry.class);
                startActivity(i);
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
       // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Online Examination");
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        Tools.setSystemBarLight(this);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        View v = nav_view.getHeaderView(0);
        Header_img = (CircularImageView) v.findViewById(R.id.image);
        txt_username = (TextView) v.findViewById(R.id.username);
        txt_class_dept = (TextView) v.findViewById(R.id.class_dept);
        txt_username.setText(first_name+" "+last_name);

        txt_class_dept.setText(student_id+"  "+class_name +"("+dept_name+")");
        String img_path = MyConfig.Parent_Url+"assets/uploads/avatar/"+avator;
        InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
        try {
            in = (InputStream) new URL(img_path).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
        Header_img.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
        try {
            if(in != null)
                in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("profile", String.valueOf(img_path));



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                actionBar.setTitle(item.getTitle());
                drawer.closeDrawers();

                String titile = String.valueOf(item.getTitle());

                if(titile.equalsIgnoreCase("Home")){

                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }else if(titile.equalsIgnoreCase("My Profile")){

                    Intent i = new Intent(MainActivity.this, EditProfile.class);
                    startActivity(i);

                }else if(titile.equalsIgnoreCase("My EnRoll")){

                    Intent i = new Intent(MainActivity.this, MyEnroll.class);
                    startActivity(i);

                }
                else if(titile.equalsIgnoreCase("My Exams")){

                    Intent i = new Intent(MainActivity.this, Examinations.class);
                    startActivity(i);

                }
                else if(titile.equalsIgnoreCase("Change Password")){

                    Intent i = new Intent(MainActivity.this, Change_Password.class);
                    startActivity(i);

                }
             /*   else if(titile.equalsIgnoreCase("Settings")){

                    Intent i = new Intent(MainActivity.this, AssessmentRecord.class);
                    startActivity(i);
                }*/
                else if(titile.equalsIgnoreCase("Rate This App")){

                  //  Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Uri uri = Uri.parse("market://details?id=com.wire");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.wire")));//Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));

                    }
                }
                else if(titile.equalsIgnoreCase("Refer Friend")){

                    String shareBody = "Use this Mobile App for free Online tests & Series Tests,Free Downloads, Exam Details & Alerts.Click On:" + " \n " + getString( R.string.url_app_google_play )+"& referrer= "+ student_id;
                    Intent sharingIntent = new Intent( android.content.Intent.ACTION_SEND );
                    sharingIntent.setType( "text/plain" );
                    sharingIntent.putExtra( android.content.Intent.EXTRA_TEXT, shareBody );
                    sharingIntent.putExtra( Intent.EXTRA_SUBJECT, getString( R.string.app_name ) );
                    startActivity( Intent.createChooser( sharingIntent, getResources().getString( R.string.app_name ) ) );
                }
                else if(titile.equalsIgnoreCase("Help")){

                    Intent i = new Intent(MainActivity.this, Help.class);
                    startActivity(i);
                }
                else if(titile.equalsIgnoreCase("Logout")){

                    sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
                    editor = sharedPreferences.edit();
                    editor.remove( "user_name" );
                    editor.remove( "password" );
                    editor.commit();
                    Intent i = new Intent(MainActivity.this, Login.class);
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);

       // Tools.changeMenuIconColor(menu, getResources().getColor(R.color.colorPrimary));
        //Tools.changeOverflowMenuIconColor(toolbar, getResources().getColor(R.color.colorPrimary));

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void GetUserdata() { String data;

        try {
            sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
            sharedPreferences.edit();
            String user_name= sharedPreferences.getString("user_name",null);
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
            HttpPost httppost = new HttpPost( MyConfig.URL_PROFILE_DATA);
            nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
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
                    student_id = obj.getString("id");
                    first_name= obj.getString("first_name");
                    last_name = obj.getString("last_name");
                    gender = obj.getString("gender");
                    department = obj.getString("department");
                    student_class = obj.getString("class");
                    email = obj.getString("email");
                    role = obj.getString("role");
                    avator = obj.getString("avator");
                    dept_name = obj.getString("dept_name");
                    class_name = obj.getString("class_name");


                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
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
                    editor.commit();

                    Log.e("fetch data",student_id+"="+first_name+"="+department+"="+student_class+"//"+avator);
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
        Exit();
    }

    public void Exit() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.backbutton))
                .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                                Intent i = new Intent();
                                i.setAction(Intent.ACTION_MAIN);
                                i.addCategory(Intent.CATEGORY_HOME);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

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

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("1", R.drawable.banner1);
        HashMapForLocalRes.put("2", R.drawable.banner2);
        HashMapForLocalRes.put("3", R.drawable.banner3);
        HashMapForLocalRes.put("4", R.drawable.banner4);
       // HashMapForLocalRes.put("5", R.drawable.banner5);

    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.my_dialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.buttonOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Use this Mobile App for free Online tests & Series Tests,Free Downloads, Exam Details & Alerts.Click On:" + " \n " + getString( R.string.url_app_google_play )+"& referrer= "+ student_id;
                Intent sharingIntent = new Intent( android.content.Intent.ACTION_SEND );
                sharingIntent.setType( "text/plain" );
                sharingIntent.putExtra( android.content.Intent.EXTRA_TEXT, shareBody );
                sharingIntent.putExtra( Intent.EXTRA_SUBJECT, getString( R.string.app_name ) );
                startActivity( Intent.createChooser( sharingIntent, getResources().getString( R.string.app_name ) ) );
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.buttonSkip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
}
