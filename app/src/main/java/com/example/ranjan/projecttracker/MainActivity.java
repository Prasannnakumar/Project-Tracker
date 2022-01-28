package com.example.ranjan.projecttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.TextView;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void button_signup_click(View v)
    {
        Intent intent=new Intent(getApplication(),signup.class);
        startActivity(intent);
    }
    public void button_signin_click(View v)
    {
        Intent intent=new Intent(getApplication(),signin.class);
        startActivity(intent);
    }
}