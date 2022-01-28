package com.example.ranjan.projecttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class imageshow extends AppCompatActivity {
    ImageView imageViewshow;
    Bitmap originalBitmap,image;
    Paint paint;
    String mydir;
    public static String imagename;
    public static String longitude;
    public static String latitude;
    EditText editText_issue,editText_date,editText_time,editText_place,editText_remark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageshow);

        latitude=String.valueOf(googlemap.latitude);
        longitude=String.valueOf(googlemap.longitude);
        imagename=String.valueOf(latitude);

        imageViewshow=(ImageView)findViewById(R.id.imageViewshow);
        editText_issue=(EditText)findViewById(R.id.editText_issue);
        editText_date=(EditText)findViewById(R.id.editText_date);
        editText_time=(EditText)findViewById(R.id.editText_time);
        editText_place=(EditText)findViewById(R.id.editText_place);
        editText_remark=(EditText)findViewById(R.id.editText_remark);
        // create image and bitmap area
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//dimentions x,y of device to create a scaled bitmap having similar dimentions to screen size
        int height1 = displaymetrics.heightPixels;
        int width1 = displaymetrics.widthPixels;
//paint object to define paint properties
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setTextSize(25);
        //String RootDir = Environment.getExternalStorageDirectory() + File.separator + "txt_imgs/Image-3180.jpg";
//loading bitmap from drawable
        mydir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(mydir);
        newdir.mkdirs();
        String file = mydir+imagename+".jpg";
        String imgfile=file;
        originalBitmap = BitmapFactory.decodeFile(imgfile);
//scaling of bitmap
        originalBitmap =Bitmap.createScaledBitmap(originalBitmap, width1, height1, false);
//creating anoter copy of bitmap to be used for editing
        image = originalBitmap.copy(Bitmap.Config.RGB_565, true);

    }
    public void button_show_click(View v)
    {
        float scr_x = 30;
        float scr_y = 200;
//funtion called to perform drawing
        createImage(scr_x, scr_y,imagename);
        saveImage(image);
    }
    void saveImage(Bitmap img) {

        File myDir=new File(mydir);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = mydir+imagename+".jpg";
        File file = new File (myDir, fname);
        String picturePath=file.toString();
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap createImage(float scr_x,float scr_y,String user_text){
        //canvas object with bitmap image as constructor
        Canvas canvas = new Canvas(image);
        int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //removing title bar hight
        scr_y=scr_y- viewTop;
        //fuction to draw text on image. you can try more drawing funtions like oval,point,rect,etc...
        canvas.drawText("" + user_text, scr_x, scr_y, paint);
        imageViewshow.setImageBitmap(image);
        return image;
    }
    public void button_save_click(View v)
    {

        String issue=editText_issue.getText().toString();
        String date=editText_date.getText().toString();
        String time=editText_time.getText().toString();
        String place=editText_place.getText().toString();
        String remark=editText_remark.getText().toString();
       // Toast.makeText(this,issue,Toast.LENGTH_SHORT).show();
        new ExecuteTask().execute(issue,date,time,place,remark,imagename,latitude,longitude);
    }
    class ExecuteTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String res=PostData(params);
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String PostData(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://10.0.2.2:8181/myproject/store.php");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("issue", valuse[0]));
            list.add(new BasicNameValuePair("date", valuse[1]));
            list.add(new BasicNameValuePair("time", valuse[2]));
            list.add(new BasicNameValuePair("place", valuse[3]));
            list.add(new BasicNameValuePair("remark", valuse[4]));
            list.add(new BasicNameValuePair("latitude",latitude ));
            list.add(new BasicNameValuePair("longitude",longitude ));
            list.add(new BasicNameValuePair("imagename", valuse[5]));

            // image send data
            BitmapDrawable drawable = (BitmapDrawable)imageViewshow.getDrawable();
            Bitmap btm = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            list.add(new BasicNameValuePair("image", encodedImage));

            //end new data
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponse(httpResponse);

        }
        catch(Exception exception)  {}
        return s;
    }

    public String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        } catch (Exception e) {

        }
        return return_text;
    }
    }
