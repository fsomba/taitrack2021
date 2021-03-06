package com.darajalab.TaiTrack;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

/**  **/
public class FragmentAttendance extends Fragment {
    //declare variables
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView txt_no_attendance;
    private ObjectMapper mapper;
    String REGNO = StudentHome.REGNO;
    String UNIT_CODE = UnitDetails.unit_code;

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
        View view= inflater.inflate(R.layout.fragment_attendance, container, false);
        //initialize view variables
        txt_no_attendance = view.findViewById(R.id.txt_no_attendance);
        recyclerView = view.findViewById(R.id.recycler_attendance);
        mapper = new ObjectMapper();
        //set the my units view
        readAttendanceFromDb();
        return view;
    }

    //refresh view when a fragment loses and gains attention
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            readAttendanceFromDb();
        }
    }

    public void readAttendanceFromDb(){
        //read server data first
        String attendance_data= "";
        try {
            attendance_data = new ReadUnitAttendance().execute(REGNO,UNIT_CODE).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<AttendanceData> dataList2 = null;

        //process the attendance data if its available
        if(attendance_data.startsWith("{")){
            dataList2=processJson(attendance_data);
        }

        //
        assert dataList2 != null;
        if(!dataList2.isEmpty()){
            Log.e("DATA","ATTENDANCE DATA FOUND");
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
            Log.e("NO DATA","NO ATTENDANCE DATA FOUND");
            txt_no_attendance.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public ArrayList<AttendanceData> processJson(String jsonString){
        ArrayList<AttendanceData> dataList = new ArrayList<>();

        //Convert JSON string to Map with key,value pairs
        Map<String, String> map = null;
        try {
            //Log.e("ROW DATA",jsonString);
            map = mapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //
        int count=0;
        assert map != null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            //remove the item numberings added by php to every [key,value] result
            if (entry.getKey().length() > 1){
                //ignore the first two [key,value] pairs which indicate REGNO and STUDENTNAME
                if(count>1){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    //save as objects of AttendanceData(Date,Value)
                    AttendanceData dataObject = new AttendanceData(key,value);
                    dataList.add(dataObject);
                    count+=1;
                }else{
                    count+=1;
                }
            }
        }
        return dataList;
    }

    //inner static class to pull attendance data for a unit
    public static class ReadUnitAttendance extends AsyncTask<String, Void, String> {
        String server_response="";

        @Override
        protected String doInBackground(String... params) {
            String REGNO = params[0];
            String UNIT_CODE =params[1];

            try{
                URL url =new URL(IpAddress.unit_attendance_url);
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