package com.darajalab.TaiTrack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CameraHome extends AppCompatActivity {
    // user defined codes for tracking permission requests
    public static final int READ_STORAGE_PERMISSION_CODE = 100;
    public static final int WRITE_STORAGE_PERMISSION_CODE = 200;
    //
    private Camera mCamera;
    Camera.Parameters camera_parameters;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private Button btnCapture, switchCamera;
    TextView txt_position_tip;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    public static Bitmap bitmap;
    private LinearLayout linearLayout_ok_retry;
    private byte[] preview_photo;
    String calling_class="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_home);
        //reference view variables
        switchCamera = findViewById(R.id.btnSwitch);
        linearLayout_ok_retry = findViewById(R.id.linearLayout_ok_retry);
        btnCapture = findViewById(R.id.btnCam);
        txt_position_tip = findViewById(R.id.txt_position_tip);
        //hide the OK and RETRY buttons
        linearLayout_ok_retry.setVisibility(View.GONE);
        /*
        temporarily disable the Switch Camera Button
         */
        switchCamera.setVisibility(View.GONE);
        //

        //
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;

        //open the front camera by default
        int cameraId = findFrontFacingCamera();
        if (cameraId >= 0) {
            //open the front Camera
            mCamera = Camera.open(cameraId);
        }else{
            //open any camera
            mCamera =  Camera.open();
        }
        //set up the camera orientation
        mCamera.setDisplayOrientation(90);
        cameraPreview = (LinearLayout) findViewById(R.id.cPreview);
        //attach the camera device to the preview
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        //attach graphic drawing of oval
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenCenterX = (size.x /2);
        int screenCenterY = (size.y/2) ;
        //call the DrawOnTop class
        DrawOnTop mDraw = new DrawOnTop(this,screenCenterX,screenCenterY);
        addContentView(mDraw, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPicture = getPictureCallback();
                mCamera.takePicture(null, null, mPicture);
                //show the OK and the RETRY BUTTONS
                linearLayout_ok_retry.setVisibility(View.VISIBLE);
                //hide the TAKE PHOTO or SIGN ATTENDANCE BUTTON
                btnCapture.setVisibility(View.GONE);
                txt_position_tip.setVisibility(View.GONE);
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of cameras
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    //release the old camera instance
                    //switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {

                }
            }
        });

        mCamera.startPreview();
        camera_parameters = mCamera.getParameters();
        camera_parameters.setRotation(90);
    }

    private int findFrontFacingCamera() {

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;

    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;

            }

        }
        return cameraId;
    }

    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }
    /* When the user pause our app by switching between various apps,
     we need to release the camera resource of android device*/
    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /* When the user comes back to our app we will get back camera in onResume() method*/
    public void onResume() {
        super.onResume();
        if(mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        }else {
            Log.d("nu","no null");
        }

    }

    private Camera.PictureCallback getPictureCallback() {

        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                preview_photo = data;
                //convert the picture into a bitmap
                //bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        };
        return picture;
    }

    //this method is called by btn OK
    public void save_photo(View view){
        //do something with the bitmap
        String server_response = "";
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null){
            Log.e("CAM ERROR", "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            //convert the picture into a bitmap
            bitmap = BitmapFactory.decodeByteArray(preview_photo, 0, preview_photo.length);
            //rotate the bitmap to 90 degrees portrait mode
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            //
            Matrix mtx = new Matrix();
            mtx.postRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
            //save the file in jpeg format
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
            fos.flush();
            //fos.write(preview_photo);
            fos.close();
            Log.e("PHOTO SAVED", "PHOTO SAVED");
            //
            //call helper class to push photo to server

        } catch (FileNotFoundException e) {
            Log.e("CAM ERROR", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("CAM ERROR", "Error accessing file: " + e.getMessage());
        }

    }
    //
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "KasukuPro");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("KasukuPro", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    //called by btn RETRY to restart the camera preview
    public void restartCamera(View view) {
        mCamera.startPreview();
        //hide the OK and the RETRY BUTTONS
        linearLayout_ok_retry.setVisibility(View.GONE);
        //show the TAKE PHOTO or SIGN ATTENDANCE BUTTON
        btnCapture.setVisibility(View.VISIBLE);
        txt_position_tip.setVisibility(View.VISIBLE);
    }

}