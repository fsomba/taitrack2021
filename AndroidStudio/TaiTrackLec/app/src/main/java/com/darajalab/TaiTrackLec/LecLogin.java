package com.darajalab.TaiTrackLec;

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

public class LecLogin extends AppCompatActivity {
    //declare variables
    EditText edt_staff_id, edt_passcode;
    String staff_id, passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_login);
        //initialize view variables
        edt_staff_id = findViewById(R.id.edt_staff_id);
        edt_passcode = findViewById(R.id.edt_passcode);
    }

    //open activity to register a student
    public void open_lec_register_activity(View view) {
        Intent intent = new Intent(this,LecRegistration.class);
        startActivity(intent);
    }

    public void verify_lec_login(View view) {
        //get values entered and //trim leading and trailing whitespaces
        staff_id = edt_staff_id.getText().toString().trim();
        passcode = edt_passcode.getText().toString().trim();
        //verify login attempt
        new VerifyLecLogin(this).execute(staff_id,passcode);
    }
    //background task class to verify login credentials from DB
    public static class VerifyLecLogin extends AsyncTask<String,Void,String> {
        //constructor
        VerifyLecLogin(Context context){
            this.context=context;
        }
        //get the server url
        String lec_login_url = IpAddress.lec_login_url;
        String server_response="";
        String username="";
        String STAFFID = "";
        Context context;

        @Override
        protected String doInBackground(String... params) {
            String staff_id=params[0];
            STAFFID = staff_id;
            String passcode = params[1];
            try{
                URL url =new URL(lec_login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("STAFFID","UTF-8")+"="+URLEncoder.encode(staff_id,"UTF-8")+"&"+
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
                Toast.makeText(context,"Welcome "+username,Toast.LENGTH_SHORT).show();
                //open lec home page
                Intent intent = new Intent(context,LecHome.class);
                intent.putExtra("username",username);
                intent.putExtra("STAFFID",STAFFID);
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