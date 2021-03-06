package com.darajalab.TaiTrack;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.darajalab.TaiTrack.StudentHome.REGNO;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**  */
public class FragmentOverview extends Fragment implements View.OnClickListener{

    double [] lecturer_gps = new double[2];
    //allowed radius in meters
    private final double allowed_radius = 20.0;

    // user defined codes for tracking permission requests
    public static final int LOCATION_PERMISSION_CODE = 100;
    public static final int CAMERA_AND_STORAGE_PERMISSION = 300;
    //declare variables
    Button button_sign_attendance, button_drop_unit;
    TextView txt_unit_code, txt_unit_name, txt_progress1;
    ProgressBar progressBar1;
    //fetch unit details from the activity
    String unit_code = UnitDetails.unit_code;
    String unit_name = UnitDetails.unit_name;
    String response="";
    String method_name = "";
    private Context context;

    public FragmentOverview() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get the layout view
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        //reference view components
        txt_unit_code = view.findViewById(R.id.txt_unit_code);
        txt_unit_name = view.findViewById(R.id.txt_unit_name);
        button_sign_attendance = view.findViewById(R.id.btnSignAttendance);
        button_drop_unit =  view.findViewById(R.id.btnDropUnit);
        //
        progressBar1 = view.findViewById(R.id.progressBar1);
        txt_progress1 = view.findViewById(R.id.txt_progress1);
        progressBar1.setVisibility(View.GONE);
        txt_progress1.setVisibility(View.GONE);
        //
        txt_unit_code.setText(unit_code);
        txt_unit_name.setText(unit_name);
        context = getContext();
        //
        button_drop_unit.setOnClickListener(this);
        button_sign_attendance.setOnClickListener(this);
        //
        return view;
    }

    //handle clicks for all buttons using their IDs
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignAttendance:
                //call its method
                sign_attendance();
                break;
            case R.id.btnDropUnit:
                //call its method
                drop_unit(view);
                break;
        }
    }

    //method to sign attendance
    public void sign_attendance() {
        //variable to hold location difference
        double location_difference = 0.0;

        //check if location service is enabled
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            assert locationManager != null;
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps_enabled) {
                //print a message and exit the method execution
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("DEVICE GPS LOCATION REQUIRED")
                        .setMessage("To Sign Attendance, Please Turn ON Your GPS Location First.")
                        .setPositiveButton("OK", null)
                        .show();
                //don't proceed. exit the sign_attendance method
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //call the location function
        getLocation();
        //sign attendance alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Attendance Signing.");
        //
        alertDialog.setMessage("Sign Attendance For: " + unit_name);

        //create and handle a positive response

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*check the location difference */
                        if (IpAddress.location_difference > allowed_radius) {
                            dialog.cancel();
                            String message = "Signing Failed. You Are Not Within Class!";
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            //Log.e("LOCATION DIFFERENCE", String.valueOf(IpAddress.location_difference));
                        } else {
                            dialog.cancel();
                            //student is within allowed radius. proceed with other checks
                            //call face verification method first
                            verify_face();
                            //Log.e("LOCATION DIFFERENCE", String.valueOf(IpAddress.location_difference));
                        }
                    }
                });

        //create and handle a negative response
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing. cancel the dialog
                        dialog.cancel();
                    }
                });

        //set alert icon
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.show();

    }

    //method to handle student dropping a unit
    public void drop_unit(View view) {
        //alert dialog to confirm unit dropping
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle("Confirm Dropping of: "+unit_code);
        //
        alertDialog.setMessage("Drop "+ unit_name + " from your units?");

        //create and handle a positive response
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete the unit from the database
                        try {
                            response = new DropStudentUnit().execute(REGNO,unit_code,unit_name).get();
                            if(response.startsWith("Unit Dropped")){
                                dialog.cancel();
                                //close the Unit page and go back to "my units"
                                getActivity().finish();
                                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                            }else{
                                dialog.cancel();
                                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

        //create and handle a negative response
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing. cancel the dialog
                        dialog.cancel();
                    }
                });

        //set alert icon
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.show();
    }

    /* method to get student latitude, longitude and calculate radius to lect position in meters*/
    public void getLocation(){
        final double[] location_difference = {0};

        //check if user has already given permission for Location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                //context compat is used to check for permissions when you extend AppCompatActivity
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            //fetch the lecturer's coordinates

            try {
                lecturer_gps =  new ReadUnitLocation().execute(unit_code).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            //get the student's location
            //
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY,null)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                    // Got current location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Location lec_location = new Location("lecturer");
                        lec_location.setLatitude(lecturer_gps[0]);
                        lec_location.setLongitude(lecturer_gps[1]);
                        //compute difference btn student location and lecturer location
                        double difference = location.distanceTo(lec_location);
                        //assign
                        location_difference[0] = difference;
                        //save the location diff in IpAddress static class
                        IpAddress.location_difference = location_difference[0];
                        //print out location data
                        Log.e("LOCATION DATA","FORMATTED AS LONGITUDE, LATITUDE");
                        Log.e("LECTURER","LONGITUDE: "+lecturer_gps[1] + ", LATITUDE: " + lecturer_gps[0]);
                        Log.e("STUDENT","LONGITUDE: "+ location.getLongitude() + ", LATITUDE: " + location.getLatitude());
                        //print out the location difference in meters
                        Log.e("LOCATION DIFFERENCE","METERS BETWEEN LECTURER AND STUDENT");
                        Log.e("LOCATION DIFFERENCE",location_difference[0] + " Meters.");
                    }else{
                        //print out location data
                        Log.e("MISSING LOCATION DATA","NO GPS");
                    }

                    }
                });

            //if permission is missing
        } else {
            // directly ask for the permission.
            //use requestPermissions() in fragments instead of ActivityCompat.requestPermissions()
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }

    }

    /*When the user responds to your app's permission request
    onRequestPermissionsResult method is called by the android system to handle the permission result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            //grantResults length is > 0 if permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //re-call the get Location method
                getLocation();
            } else {
                Log.i( "LOCATION DENIED","About to Display Deny Dialog.");
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("LOCATION REQUIRED!")
                        .setMessage("KasukuPro Requires Location Permission")
                        .setPositiveButton("OK", null)
                        .show();
                Log.i( "LOCATION DENIED","Deny Dialog Shown.");
            }
        }else if (requestCode == CAMERA_AND_STORAGE_PERMISSION) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //call verify_face again
                verify_face();
            }else{
                Toast.makeText(getContext(),"Camera and Storage Permission Needed.",Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class DropStudentUnit extends AsyncTask<String, Void, String> {
        String server_response="";

        @Override
        protected String doInBackground(String... params) {
            String REGNO = params[0];
            String UNIT_CODE =params[1];
            String UNIT_NAME =params[2];

            try{
                URL url =new URL(IpAddress.drop_unit_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("REGNO","UTF-8")+"="+URLEncoder.encode(REGNO,"UTF-8")+"&"+
                        URLEncoder.encode("UNIT_CODE","UTF-8")+"="+URLEncoder.encode(UNIT_CODE,"UTF-8")+"&"+
                        URLEncoder.encode("UNIT_NAME","UTF-8")+"="+URLEncoder.encode(UNIT_NAME,"UTF-8");

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
                server_response = "Unit Drop failed!";
                e.printStackTrace();
            }
            return server_response;
        }
    }

    /* static class to get latitude, longitude for sch unit from DB*/
    public static class ReadUnitLocation extends AsyncTask<String,Void,double[]>{
        String server_response="";
        double [] gps_coordinates = new double [2];

        @Override
        protected double[] doInBackground(String... params) {
            String UNIT_CODE =params[0];

            try{
                URL url =new URL(IpAddress.read_unit_location_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("UNIT_CODE","UTF-8")+"="+URLEncoder.encode(UNIT_CODE,"UTF-8") ;

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();

                //read the php output of latitude @ longitude
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
                //parse the server response to get the latitude, longitude
                Log.i("LOCATION DATA",server_response);
                String[] location = server_response.split("@");
                //
                gps_coordinates[0] = Double.parseDouble(location[0]);
                gps_coordinates [1] = Double.parseDouble(location[1]);

            } catch (IOException e){
                server_response = "Error Occurred";
                e.printStackTrace();
            }
            return gps_coordinates;
        }

    }

    //method to verify face. Called by Button Verify Face
    public void verify_face(){
        //set name of the method requesting CAMERA permission
        method_name = "verify_face";
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

    //handle callbacks from previous activity that is closing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //upload photo to server
        if(requestCode==0 && resultCode == RESULT_OK){
            //get photo path from the closed camera activity
            String photo_path = data.getStringExtra("photo_path");
            //upload the photo to server
            new UploadPhoto(getActivity(),method_name).execute(photo_path);
        }

    }
}