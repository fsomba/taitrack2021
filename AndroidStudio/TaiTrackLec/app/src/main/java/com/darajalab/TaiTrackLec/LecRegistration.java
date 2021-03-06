package com.darajalab.TaiTrackLec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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

public class LecRegistration extends AppCompatActivity {
    //declare variables
    EditText edt_staffid, edt_firstname, edt_secondname, edt_email, edt_passcode;
    Spinner spinner_title;
    //
    String staff_id, first_name, second_name, title, email, passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_registration);
        //initialize view variables
        edt_staffid = findViewById(R.id.edtStaffId);
        edt_firstname = findViewById(R.id.edtFirstName);
        edt_secondname = findViewById(R.id.edtSecondName);
        spinner_title = findViewById(R.id.spinner_title);
        edt_email = findViewById(R.id.edtEmail);
        edt_passcode = findViewById(R.id.edtPasscode);
    }

    public void register_lec(View view) {
        //get values entered in view boxes
        staff_id = edt_staffid.getText().toString().trim();
        first_name=edt_firstname.getText().toString().trim();
        second_name=edt_secondname.getText().toString().trim();
        title = spinner_title.getSelectedItem().toString();
        email=edt_email.getText().toString().trim();
        passcode = edt_passcode.getText().toString().trim();

        //commit values to the database
        new SaveLecDetails(this).execute(staff_id,first_name,second_name,title,email,passcode);
    }
    //background class to push values to the DB
    public static class SaveLecDetails extends AsyncTask<String, Void, String> {
        //get the script url
        String lec_register_url = IpAddress.lec_register_url;
        String server_response="";
        String username;
        Context context;
        //constructor
        SaveLecDetails(Context context){
            this.context=context;
        }

        @Override
        protected String doInBackground(String... params) {
            String staff_id = params[0];
            String first_name=params[1];
            username=first_name;
            String second_name=params[2];
            String title=params[3];
            String email=params[4];
            String passcode = params[5];
            try{
                URL url =new URL(lec_register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("STAFFID","UTF-8")+"="+URLEncoder.encode(staff_id,"UTF-8")+"&"+
                        URLEncoder.encode("FIRSTNAME","UTF-8")+"="+URLEncoder.encode(first_name,"UTF-8")+"&"+
                        URLEncoder.encode("SECONDNAME","UTF-8")+"="+URLEncoder.encode(second_name,"UTF-8")+"&"+
                        URLEncoder.encode("TITLE","UTF-8")+"="+URLEncoder.encode(title,"UTF-8")+"&"+
                        URLEncoder.encode("EMAIL","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("PASSCODE","UTF-8")+"="+URLEncoder.encode(passcode,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();

                //read the php output on registration status
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String response= "";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    response+=line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                //
                server_response = response;
            } catch (IOException e){
                server_response = "Registration failed!";
                e.printStackTrace();
            }
            return server_response;
        }

        @Override
        protected void onPostExecute(String result) {
            //if registration success
            if (result.equals("Registration Success")) {
                //alert dialog for registration success
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Registration Success!");
                //
                alertDialog.setMessage("Welcome "+username);
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //open landlord home page
                        Intent intent = new Intent(context,LecHome.class);
                        context.startActivity(intent);
                    }
                });
                alertDialog.show();

                //if registration fails. show message box
            } else if (result.startsWith("Registration failed!")) {
                //alert dialog for registration failed
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Registration Feedback");
                //
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }
    }
}