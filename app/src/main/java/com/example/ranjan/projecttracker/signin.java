package com.example.ranjan.projecttracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class signin extends AppCompatActivity {
    EditText editTextuser_id,editTextpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        editTextuser_id=(EditText)findViewById(R.id.editTextuser_id);
        editTextpassword=(EditText)findViewById(R.id.editTextpassword);
    }
    public void button_login_click(View v)
    {
        String uid=editTextuser_id.getText().toString();
        String pwd=editTextpassword.getText().toString();
        new ExecuteTask().execute(uid,pwd);
    }
    class ExecuteTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            return PostData(params);
        }
        protected void onPostExecute(String result){
            if(result.trim().equals("success")){
                Intent intent=new Intent(getApplication(),userhome.class);
                intent.putExtra("user_id",editTextuser_id.getText().toString());
                startActivity(intent);
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
    public String PostData(String[] values) {
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.201:8181/myproject/login.php");
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("user_id", values[0]));
            list.add(new BasicNameValuePair("password", values[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            s = readResponse(httpResponse);
        } catch (Exception exception) {
        }
        return s;
    }
    public String readResponse(HttpResponse res){
        InputStream is = null;
        String return_text = "";
        try {
            is =res.getEntity().getContent();
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
