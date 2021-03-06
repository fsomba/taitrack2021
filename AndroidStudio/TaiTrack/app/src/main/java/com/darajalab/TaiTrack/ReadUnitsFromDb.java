package com.darajalab.TaiTrack;

import android.content.Context;
import android.os.AsyncTask;
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

/**
 * This class updates UI of My Units after reading the server data available
 */
public class ReadUnitsFromDb extends AsyncTask<String, Void, String> {
    Context context;
    String script_url;
    String server_response ="";

    //constructor
    ReadUnitsFromDb(Context context, String script_url)
    {
        this.context=context;
        this.script_url = script_url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String REGNO=params[0];
        try{
            URL url =new URL(script_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("REGNO","UTF-8")+"="+URLEncoder.encode(REGNO,"UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder response= new StringBuilder();
            String line="";
            while((line=bufferedReader.readLine())!=null){
                response.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            //
            server_response = response.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return server_response;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
