package com.darajalab.TaiTrack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**  */
public class FragmentMyProfile extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private ImageView imageView_student;
    private Button btn_update_photo;
    public ProgressBar progressBar;
    public TextView txt_progress;

    //replace all the characters except alphanumeric in the REGNO
    String regno = IpAddress.regno.replaceAll("[^A-Za-z0-9]","");
    //
    String method_name="";
    //
    String photoUrl= IpAddress.server_ip+"/kasukupro/PythonServer/photos/"+ regno + ".jpg";
    // user defined codes for tracking permission requests
    // for camera and write storage permission requests
    public static final int CAMERA_AND_STORAGE_PERMISSION = 300;

    public FragmentMyProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        //reference views
        imageView_student = view.findViewById(R.id.image_student);
        progressBar = view.findViewById(R.id.progressBar);
        txt_progress = view.findViewById(R.id.txt_progress);
        progressBar.setVisibility(View.GONE);
        txt_progress.setVisibility(View.GONE);
        btn_update_photo = view.findViewById(R.id.btnUpdatePhoto);
        btn_update_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_photo();
            }
        });

        //set current student photo
        loadPhoto();
        //
        return view;
    }
    //refresh view when a fragment loses and gains attention
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            //refresh the photo image view
            //loadPhoto();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //refresh the photo
        loadPhoto();
    }

    //method to load photo using picasso library
    public void loadPhoto(){
        //use picasso library to load images faster
        //clear the cache to show updated image
        Picasso.get().invalidate(photoUrl);
        Picasso.get().load(photoUrl).fit().centerInside().into(imageView_student, new Callback() {
            @Override
            public void onSuccess() {
                //pass
            }

            @Override
            public void onError(Exception e) {
                Log.e("PHOTO LOAD FAILED",photoUrl);
                //use the default icon
                //imageView_student.setImageResource(R.drawable.student_icon);
                Picasso.get().load(R.drawable.student_icon).fit().centerCrop().into(imageView_student);
            }
        });
    }

    //method to change student photo. Called by Button Update
    public void update_photo(){
        //set name of the method requesting CAMERA permission
        method_name = "update_photo";
        //check if user has already given permission for camera and storage
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                //context compat is used to check for permissions when you extend AppCompatActivity
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED)
        ) {
            // proceed to read image
            Intent intent = new Intent(getContext(),CameraTakePhoto.class);
            intent.putExtra("method",method_name);
            startActivityForResult(intent,0);
            //if permission is missing
        } else {
            // directly ask for the permissions
            //use requestPermissions() in fragments instead of ActivityCompat.requestPermissions()
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_AND_STORAGE_PERMISSION);
        }
    }

    /*When the user responds to your app's permission request
    onRequestPermissionsResult method is called by the android system to handle the permission result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_AND_STORAGE_PERMISSION) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //recall method to open camera
                update_photo();
            }else{
                Toast.makeText(getContext(),"Camera and Storage Permission Needed.",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //upload photo to server
        if(requestCode==0 && resultCode == RESULT_OK){
            //get photo path from the closed camera activity
            String photo_path = data.getStringExtra("photo_path");
            //upload the photo to server
            new UploadPhoto(getActivity(),method_name).execute(photo_path);
            //refresh photo view
            loadPhoto();
        }

    }

}