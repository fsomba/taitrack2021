package com.darajalab.TaiTrack;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.darajalab.TaiTrack.StudentHome.REGNO;

/* this class updates the student photo on the server*/
public class UploadPhoto extends AsyncTask<String, Void, String>{
    //caller activity variable
    WeakReference<Activity> activityWeakReference;
    private String caller_method="";

    //constructor
    UploadPhoto(Activity caller_activity, String caller_method){
        this.activityWeakReference = new WeakReference<>(caller_activity);
        this.caller_method = caller_method;
    }

    private String server_response="";
    //replace all the characters except alphanumeric in the REGNO
    String regno_string = StudentHome.REGNO.replaceAll("[^A-Za-z0-9]","");

    //forces a thread to wait for another before resuming operation
    CountDownLatch countDownLatch = new CountDownLatch(1);

    //method to push photo to server
    @Override
    protected String doInBackground(String... params) {
        String student_photo_path = params[0];

        //prepares the message to be sent to the server
        String postUrl= IpAddress.server_ip+":5000/";
        //
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Read BitMap by file path
        Bitmap bitmap = BitmapFactory.decodeFile(student_photo_path, options);
        //compress using JPEG format
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //
        byte[] byteArray = stream.toByteArray();
        RequestBody postBodyImage = null;
        if(caller_method.equals("update_photo")){
            //create request body using Multipart which allows sending multi-part data in the HTTP
            postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //(field name to be used on server for retrieval, filename,body)
                    .addFormDataPart("image", REGNO,
                            RequestBody.create(byteArray, MediaType.parse("image/*jpg")))
                    .build();
        }else if(caller_method.equals("verify_face")){
            //create request body using Multipart which allows sending multi-part data in the HTTP
            postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //(field name to be used on server for retrieval, filename,body)
                    .addFormDataPart("image", REGNO+"test",
                            RequestBody.create(byteArray, MediaType.parse("image/*jpg")))
                    .build();
        }

        /* send request to server
        //define a client to be used
        //use a single client for all HTTP calls */
        OkHttpClient client = new OkHttpClient.Builder()
                //https://www.baeldung.com/okhttp-timeouts
                .connectTimeout(10, TimeUnit.SECONDS)
                //wait server response for 20 seconds as writing image is being done
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                //the server url
                .url(postUrl)
                //the request
                .post(postBodyImage)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            //if communicating to server fails
            public void onFailure(@NotNull Call call, final IOException e) {
                server_response = "Upload Failed. Connecting to Server Failed";
                Log.e("SERVER CONN ERROR",e.toString());
                // Cancel the post on failure.
                call.cancel();
                countDownLatch.countDown();
            }

            //when server responds to request
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
                try {
                    server_response = Objects.requireNonNull(response.body()).string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }

        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //
        return server_response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //show progress bar
        Activity activity = activityWeakReference.get();
        if (caller_method.equals("verify_face") && activity != null) {
            ProgressBar progressBar = activity.findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.VISIBLE);
            TextView txt_progress = activity.findViewById(R.id.txt_progress1);
            txt_progress.setText("...uploading photo...");
            txt_progress.setVisibility(View.VISIBLE);
            //
        }else if (caller_method.equals("update_photo") && activity != null) {
            ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            TextView txt_progress = activity.findViewById(R.id.txt_progress);
            txt_progress.setText("...uploading photo...");
            txt_progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //access the UI interface from here
        //begin verification if image has uploaded successfully
        if(caller_method.equals("verify_face") && result.startsWith("Image Uploaded")){
            //
            start_face_verification();
        }else if(caller_method.equals("verify_face") && result.startsWith("Upload Failed.")){
            //hide progress bar and print error
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                ProgressBar progressBar = activity.findViewById(R.id.progressBar1);
                progressBar.setVisibility(View.GONE);
                //print result
                TextView txt_progress = activity.findViewById(R.id.txt_progress1);
                txt_progress.setText("Photo Upload & Verification Failed. Server Connection Issue.");
                txt_progress.setVisibility(View.VISIBLE);
            }
        }else if(caller_method.equals("update_photo")){
            //hide progress bar
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                ProgressBar progressBar = activity.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                //print result
                TextView txt_progress = activity.findViewById(R.id.txt_progress);
                txt_progress.setText(result);
                txt_progress.setVisibility(View.VISIBLE);
            }
        }
    }

    /* method to start face verification/comparison
    this method calls a python script and waits for a reply*/
    public void start_face_verification(){
        String verification_result = "verification_beginning";
        final Activity activity = activityWeakReference.get();
        ProgressBar progressBar = null;
        TextView txt_progress = null;
        if (activity != null) {
            progressBar = activity.findViewById(R.id.progressBar1);
            txt_progress = activity.findViewById(R.id.txt_progress1);
            //show progress bar as verification begins
            progressBar.setVisibility(View.VISIBLE);
            txt_progress.setText("...face verification going on...");
            txt_progress.setVisibility(View.VISIBLE);
        }
        //final variables for inner class access
        final ProgressBar finalProgressBar = progressBar;
        final TextView finalTxt_progress = txt_progress;

        //prepares the message to be sent to the server
        String postUrl= IpAddress.server_ip+":5000/verify";
        //
        RequestBody postBodyString;

        //create request body using Multipart which allows sending multi-part data in HTTP
        postBodyString = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //(field name to be used on server for retrieval, value)
                .addFormDataPart("REGNO", regno_string)
                .build();

        /* send request to server
        //define a client to be used
        //use a single client for all HTTP calls */
        OkHttpClient client = new OkHttpClient.Builder()
                //https://www.baeldung.com/okhttp-timeouts
                //wait server response for 35 seconds
                .readTimeout(35, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                //the server url
                .url(postUrl)
                //the request
                .post(postBodyString)
                .build();
        //
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            //if communicating to server fails
            public void onFailure(@NotNull Call call, final IOException error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("VERIFY FAILED","VERIFICATION FAILED");
                        Log.e("OKHTTP ERROR", String.valueOf(error));
                        server_response = "PHOTO VERIFICATION FAILED";
                        //hide the progress bar
                        finalProgressBar.setVisibility(View.GONE);
                        //set the error
                        finalTxt_progress.setText(server_response);
                        //Toast.makeText(activity, server_response,Toast.LENGTH_LONG).show();
                    }
                });

            }

            //when server responds to request
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            server_response = response.body().string();
                            //print the response
                            Log.e("BACKGROUND TASK SUCCESS",server_response);
                            //hide the progress bar
                            finalProgressBar.setVisibility(View.GONE);
                            //check whether photos matched
                            if(server_response.startsWith("face is a match")){
                                //print out
                                finalTxt_progress.setText(server_response);
                                //call SignAttendance class to update DB
                                new SignAttendance(activity).execute(REGNO, UnitDetails.unit_code);
                            }else if(server_response.startsWith("face is NOT a match")){
                                //print out
                                finalTxt_progress.setText(server_response);
                                //Toast.makeText(activity, server_response,Toast.LENGTH_LONG).show();
                            }else{
                                //print out the error
                                finalTxt_progress.setText(server_response);
                                //Toast.makeText(activity, server_response,Toast.LENGTH_LONG).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            //
        });

    }

}

