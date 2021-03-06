package com.darajalab.TaiTrack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class StudentLogin extends AppCompatActivity {
    //declare variables
    EditText edt_regno, edt_passcode;
    static String regno;
    String passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        //initialize view variables
        edt_regno = findViewById(R.id.edt_regno);
        edt_passcode = findViewById(R.id.edt_passcode);
    }

    //open activity to register a student
    public void open_student_register_activity(View view) {
        Intent intent = new Intent(this,StudentRegistration.class);
        startActivity(intent);
    }

    public void verify_student_login(View view) {
        //get values entered
        regno = edt_regno.getText().toString().trim();
        passcode = edt_passcode.getText().toString().trim();
        //verify login attempt
        new VerifyStudentLogin(this).execute(regno,passcode);
    }
    //background task class to verify login credentials from DB
    public static class VerifyStudentLogin extends AsyncTask<String,Void,String> {
        //constructor
        VerifyStudentLogin(Context context){
            this.context=context;
        }
        //get the server url
        String student_login_url = IpAddress.student_login_url;
        String server_response="";
        String username="";
        Context context;

        @Override
        protected String doInBackground(String... params) {
            String regno=params[0];
            String passcode = params[1];
            try{
                URL url =new URL(student_login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("REGNO","UTF-8")+"="+URLEncoder.encode(regno,"UTF-8")+"&"+
                        URLEncoder.encode("PASSCODE","UTF-8")+"="+URLEncoder.encode(passcode,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response= "";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    response+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //
                server_response = response;
            } catch (IOException e){
                e.printStackTrace();
            }
            return server_response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if login success
            if (result.startsWith("Login success")) {
                String [] values = result.split(" ");
                username = values[2];
                Toast.makeText(context,"Welcome "+username,Toast.LENGTH_LONG).show();
                //open student home page
                Intent intent = new Intent(context,StudentHome.class);
                intent.putExtra("username",username);
                intent.putExtra("REGNO",regno);
                context.startActivity(intent);

                //if login fails. show message box
            } else if (result.startsWith("Login failed!")) {
                //alert dialog for login failed
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Login Feedback");
                //
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }
    }
}