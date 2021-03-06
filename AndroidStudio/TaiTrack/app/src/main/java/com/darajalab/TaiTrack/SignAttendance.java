package com.darajalab.TaiTrack;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignAttendance extends AsyncTask<String, Void, String> {
    String server_response="";
    //caller activity variable
    WeakReference<Activity> activityWeakReference;

    //constructor
    SignAttendance(Activity caller_activity){
        this.activityWeakReference = new WeakReference<>(caller_activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String REGNO = params[0];
        String UNIT_CODE =params[1];

        try{
            URL url =new URL(IpAddress.sign_attendance_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("REGNO","UTF-8")+"="+URLEncoder.encode(REGNO,"UTF-8")+"&"+
                    URLEncoder.encode("UNIT_CODE","UTF-8")+"="+URLEncoder.encode(UNIT_CODE,"UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();

            //read the php output on unit drop status
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
            server_response = "Signing Failed";
            e.printStackTrace();
        }
        return server_response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //
        Activity activity = activityWeakReference.get();
        TextView txt_progress = null;
        if (activity != null) {
            //print result
            txt_progress = activity.findViewById(R.id.txt_progress1);
            txt_progress.setVisibility(View.VISIBLE);
        }
        if (result.startsWith("Attendance Signed")) {
            txt_progress.setText(result);
            //Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
        } else if (result.startsWith("An Error Occured! UPDATE command denied")) {
            String message = "Signing Turned OFF by Lecturer.";
            txt_progress.setText(message);
            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        } else if (result.startsWith("An Error Occured! Unknown column")) {
            String message = "Signing Turned OFF by Lecturer.";
            txt_progress.setText(message);
            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        } else {
            txt_progress.setText(result);
            //Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
        }
    }
}
