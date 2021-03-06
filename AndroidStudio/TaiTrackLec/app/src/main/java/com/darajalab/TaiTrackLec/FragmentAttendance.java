package com.darajalab.TaiTrackLec;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FragmentAttendance extends Fragment {
    //initialize variables
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView txt_no_attendance;

    public FragmentAttendance() {
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
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        //initialize view variables
        txt_no_attendance = view.findViewById(R.id.txt_no_attendance);
        recyclerView = view.findViewById(R.id.recycler_attendance);
        //set the attendance records view
        readUnitAttendanceList();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        //refresh the home view
        readUnitAttendanceList();
    }

    public void readUnitAttendanceList(){
        //read server data first
        String attendance_data= "";
        try {
            attendance_data = new ReadUnitAttendance().execute(UnitDetails.unit_code).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<AttendanceData> dataList2 = null;

        //process the attendance data if its available
        dataList2=processJson(attendance_data);

        //
        if(!dataList2.isEmpty()){
            //hide the text view
            txt_no_attendance.setVisibility(View.GONE);
            //show recylerview
            adapter = new AttendanceRecyclerAdapter(dataList2);
            layoutManager=new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }else{
            //hide recycler
            recyclerView.setVisibility(View.GONE);
            txt_no_attendance.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<AttendanceData> processJson(String jsonString){
        ArrayList<AttendanceData> dataList = new ArrayList<>();
        //
        ObjectMapper mapper = new ObjectMapper();
        //json variables
        JSONArray jsonArray;
        JSONObject jsonObject;
        /* extract JSON result into java ArrayList*/
        try {
            jsonObject = new JSONObject(jsonString);
            //get the outer array named server_response
            jsonArray = jsonObject.getJSONArray("server_response");
            int ArrayCount = 0;
            //
            while (ArrayCount < jsonArray.length()) {
                //variables for each record
                String regno = null, student_name = null;
                //counters for PRESENT and ABSENT values
                int present = 0, absent = 0;
                //
                JSONArray jsonArray1 ;
                JSONObject json_object2 ;
                String json_row = "";
                //access the inner arrays by index
                jsonArray1 = jsonArray.getJSONArray(ArrayCount);
                 //retrieve the json object from the array of format [{string inside here}]
                json_object2 = jsonArray1.getJSONObject(0);
                //convert the json object to a string to remove the square brackets
                json_row = json_object2.toString();

                //Convert JSON string to Map with key,value pairs
                Map<String, String> map = null;
                try {
                    //
                    map = mapper.readValue(json_row, Map.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                //
                int count=0;
                assert map != null;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    //ignore the item numberings added by php to every [key,value] result
                    if (entry.getKey().length() > 1){
                        //get the first two [key,value] pairs which indicate REGNO and STUDENTNAME
                        if(count==0){
                            //get the REGNO
                            regno = entry.getValue();
                            count+=1;
                        }else if(count==1){
                            //get the STUDENT NAME
                            student_name = entry.getValue();
                            count+=1;
                        }else{
                            //get the PRESENT & ABSENT values and count them
                            String value = entry.getValue();
                            if(value.equals("PRESENT")){
                                present += 1;
                            }else if(value.equals("ABSENT")){
                                absent += 1;
                            }
                            count+=1;
                        }
                    }
                }
                //save as objects of AttendanceData(regno, student_name, present, absent)
                AttendanceData dataObject = new AttendanceData(regno,student_name,
                        String.valueOf(present),String.valueOf(absent));
                dataList.add(dataObject);
                //increment the counter for the loop
                ArrayCount++;
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //static class to Read Unit Attendance From The Db
    public static class ReadUnitAttendance extends AsyncTask<String, Void, String> {
        String server_response="";

        @Override
        protected String doInBackground(String... params) {
            String UNIT_CODE =params[0];

            try{
                URL url =new URL(IpAddress.view_unit_attendance);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("UNIT_CODE","UTF-8")+"="+URLEncoder.encode(UNIT_CODE,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();

                //read the php output off unit attendance string
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
                server_response = "Reading Data failed!";
                e.printStackTrace();
            }
            return server_response;
        }
    }
}