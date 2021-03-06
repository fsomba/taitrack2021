package com.darajalab.TaiTrackLec;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import static com.darajalab.TaiTrackLec.LecHome.STAFFID;

/**
 * A s
 */
public class FragmentOverview extends Fragment implements View.OnClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private Double Latitude,Longitude;

    // user defined codes for tracking permission requests
    public static final int LOCATION_PERMISSION_CODE = 100;

    public FragmentOverview() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    Button button_signing_on, button_signing_off, button_drop_unit;
    TextView txt_unit_code, txt_unit_name;
    String response;
    //get unit values from the UnitDetails Activity
    String unit_code = UnitDetails.unit_code;
    String unit_name = UnitDetails.unit_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get the view
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        //get components
        txt_unit_code = v.findViewById(R.id.txt_unit_code);
        txt_unit_name = v.findViewById(R.id.txt_unit_name);
        button_signing_on = v.findViewById(R.id.btnTurnOnSigning);
        button_signing_off = v.findViewById(R.id.btnTurnOffSigning);
        button_drop_unit = v.findViewById(R.id.btnDropUnit);
        //
        button_signing_on.setOnClickListener(this);
        button_signing_off.setOnClickListener(this);
        button_drop_unit.setOnClickListener(this);
        //set unit name and code
        txt_unit_code.setText("UNIT CODE: " + unit_code);
        txt_unit_name.setText("UNIT NAME: " + unit_name);
        return v;
    }

    //handle clicks for all buttons using their IDs
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTurnOnSigning:
                //call the getLocation method first and let it call the turn_on_signing() method
                getLocation();
                //turn_on_signing(view);
                break;
            case R.id.btnTurnOffSigning:
                //call its method
                turn_off_signing(view);
                break;
            case R.id.btnDropUnit:
                //call its method
                lec_drop_unit(view);
                break;
        }
    }

    public void turn_on_signing(){
        //alert dialog to confirm TURN ON SIGNING
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("TURN ON SIGNING?");
        //
        alertDialog.setMessage("Turn ON Signing For: "+ unit_name + " ?");

        //create and handle a positive response
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //GRANT TABLE UPDATE RIGHTS
                        try {
                            response = new TurnSigningOnAndOff().execute("turn_on_signing",
                                    unit_code,String.valueOf(Latitude),String.valueOf(Longitude)).get();
                            if(response.startsWith("Signing Turned ON")) {
                                dialog.cancel();
                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            }else{
                                dialog.cancel();
                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
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

    public void turn_off_signing(View view){
        //alert dialog to confirm TURN OFF SIGNING
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle("TURN OFF SIGNING?");
        //
        alertDialog.setMessage("Turn OFF Signing For: "+ unit_name + " ?");

        //create and handle a positive response
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //REVOKE TABLE UPDATE RIGHTS
                        try {
                            response = new TurnSigningOnAndOff().execute("turn_off_signing",unit_code).get();
                            if(response.startsWith("Signing Turned OFF")) {
                                dialog.cancel();
                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            }else if(response.startsWith("An Error Occured! There is no such grant defined")) {
                                String message = "Signing Already Turned OFF!";
                                dialog.cancel();
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            } else{
                                dialog.cancel();
                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
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
    //method to handle unit dropping
    public void lec_drop_unit(View view){
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
                            response = new DropLecUnit().execute(STAFFID,unit_code).get();
                            if(response.startsWith("Unit Dropped")){
                                dialog.cancel();
                                //close the Unit Overview Fragment and go back to "my units"
                                getActivity().finish();
                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                            }else{
                                dialog.cancel();
                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
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

    //method to get latitude,longitude
    public void getLocation(){
        //check if location service is enabbled
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            assert locationManager != null;
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER );
            if (!gps_enabled){
                //print a message and exit the method execution
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("TURN ON DEVICE LOCATION")
                        .setMessage("Please Turn ON Your GPS Location From Device Settings.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        //check if user has already given permission for Location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                //context compat is used to check for permissions when you extend AppCompatActivity
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            //
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Get current location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Latitude = location.getLatitude();
                        Longitude = location.getLongitude();
                        //call the turn_on_signing method
                        turn_on_signing();
                    }
                }
            });

            //if permission is missing
        } else {
            // directly ask for the permission.
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
                        .setMessage("KasukuLec Requires Location Permission")
                        .setPositiveButton("OK", null)
                        .show();
                Log.i( "LOCATION DENIED","Deny Dialog Shown.");
            }
        }
    }

    //check if location services are enabled


    public static class DropLecUnit extends AsyncTask<String, Void, String> {
        String server_response="";

        @Override
        protected String doInBackground(String... params) {
            String STAFFID = params[0];
            String UNIT_CODE =params[1];

            try{
                URL url =new URL(IpAddress.lec_drop_unit_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("STAFFID","UTF-8")+"="+URLEncoder.encode(STAFFID,"UTF-8")+"&"+
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
                server_response = "Unit Drop failed!";
                e.printStackTrace();
            }
            return server_response;
        }
    }

    //class to initiate signing turn ON or OFF
    public static class TurnSigningOnAndOff extends AsyncTask<String, Void, String> {
        String server_response="";

        @Override
        protected String doInBackground(String... params) {
            String method = params [0];
            String UNIT_CODE =params[1];
            String method_url = null;
            String latitude = "", longitude = "";
            
            //check which method called the class
            if (method.equals("turn_on_signing")){
                method_url = IpAddress.turn_signing_on;
                //get the location coordinates
                latitude = params[2];
                longitude = params[3];
            }else if(method.equals("turn_off_signing")){
                method_url = IpAddress.turn_signing_off;
            }

            try{
                URL url =new URL(method_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("UNIT_CODE","UTF-8")+"="+URLEncoder.encode(UNIT_CODE,"UTF-8")
                        +"&"+ URLEncoder.encode("LATITUDE","UTF-8")+"="+URLEncoder.encode(latitude,"UTF-8")
                        +"&"+ URLEncoder.encode("LONGITUDE","UTF-8")+"="+URLEncoder.encode(longitude,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();

                //read the php output on turn ON or OFF status
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
                server_response = "Error Occurred Sending Data To Server!";
                e.printStackTrace();
            }
            return server_response;
        }
    }
}