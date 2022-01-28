package com.example.ranjan.projecttracker;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class signup extends AppCompatActivity {
    EditText user_id,Password,security_question,answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        user_id=(EditText)findViewById(R.id.editTextuser_id);
        Password=(EditText)findViewById(R.id.editTextPassword);
        security_question=(EditText)findViewById(R.id.editTextsecurity_question);
        answer=(EditText)findViewById(R.id.editTextanswer);
    }
    public void button_save_click(View v)
    {
        String uid=user_id.getText().toString();
        String pwd=Password.getText().toString();
        String seq=security_question.getText().toString();
        String ans=answer.getText().toString();
        new ExecuteTask().execute(uid,pwd,seq,ans );
    }
    class ExecuteTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String res=PostData(params);
            return res;
        }
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }
    }
    public String PostData(String[] values) {
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.201 :8181/myproject/register.php");
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("user_id", values[0]));
            list.add(new BasicNameValuePair("password", values[1]));
            list.add(new BasicNameValuePair("security_question", values[2]));
            list.add(new BasicNameValuePair("answer", values[3]));
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