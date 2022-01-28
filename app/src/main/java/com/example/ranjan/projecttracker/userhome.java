package com.example.ranjan.projecttracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class userhome extends AppCompatActivity {

    public static String user_id;
    public static String firstimagename="12345678";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        Intent intent=getIntent();
        user_id=intent.getStringExtra("user_id");
    }
    public void button_map_click(View v)
    {
        Intent intent=new Intent(getApplication(),googlemap.class);
        startActivity(intent);
    }
    public void button_camera_click(View v){
       String mydir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(mydir);
        newdir.mkdirs();
        firstimagename=String.valueOf(googlemap.latitude);
        String file = mydir+firstimagename+".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();

        } catch (IOException e) {}
        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent,0);
    }
    public void button_save_click(View v)
    {
        Intent intent=new Intent(getApplication(),imageshow.class);
        startActivity(intent);
    }
}
