package com.darajalab.TaiTrackLec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddUnits extends AppCompatActivity {
    //initialize variables
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView txt_no_units;
    Spinner spinner_courses;
    String current_course="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_units);
        //initialize view variables
        txt_no_units = findViewById(R.id.txt_no_units);
        recyclerView = findViewById(R.id.recycler_add_units);
        spinner_courses = findViewById(R.id.spinner_courses);

        //*** set a default value for the course name **//
        current_course = getResources().getStringArray(R.array.focim_courses)[0];
        //set the my units view
        readMyUnitsFromDb();

        //listen to changes in the courses dropdown list
        spinner_courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get the selected item
                current_course = spinner_courses.getSelectedItem().toString();
                //read the db again
                readMyUnitsFromDb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //refresh the home view
        readMyUnitsFromDb();
    }

    public void readMyUnitsFromDb(){
        //read server data first
        ReadUnitsFromDb readUnitsFromDb = new ReadUnitsFromDb(this,IpAddress.lec_add_units_url);
        String unit_data="";
        ArrayList<UnitData> dataList2;

        try {
            unit_data = readUnitsFromDb.execute("COURSE_NAME",current_course).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //
        //process the cardata
        dataList2=processJson(unit_data);
        //
        if(dataList2.size()>0){
            //hide the text view
            txt_no_units.setVisibility(View.GONE);
            //show recylerview
            adapter = new AddUnitsRecyclerAdapter(dataList2,current_course);
            layoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }else{
            //hide recycler
            recyclerView.setVisibility(View.GONE);
            txt_no_units.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<UnitData> processJson(String values){
        ArrayList<UnitData> dataList;
        dataList=new ArrayList<>();
        String json_string2;
        //json variables
        JSONArray jsonArray;
        JSONObject jsonObject;
        /* extract JSON result into java ArrayList*/
        json_string2 = values;
        try {
            jsonObject = new JSONObject(json_string2);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String unit_code,unit_name;
            //
            while (count < jsonArray.length()) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                unit_code = jsonObject2.getString("UNIT_CODE");
                unit_name = jsonObject2.getString("UNIT_NAME");
                //save as objects of UnitData
                UnitData dataObject = new UnitData(unit_code,unit_name);
                dataList.add(dataObject);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}