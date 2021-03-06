package com.darajalab.TaiTrackLec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.darajalab.TaiTrackLec.LecHome.STAFFID;

public class MyUnits extends AppCompatActivity {
    //initialize variables
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView txt_no_units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_units);
        //initialize view variables
        txt_no_units = findViewById(R.id.txt_no_units);
        recyclerView = findViewById(R.id.recycler_myunits);
        //set the my units view
        readMyUnitsFromDb();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        //refresh the home view
        readMyUnitsFromDb();
    }

    public void readMyUnitsFromDb(){
        //read server data first
        ReadUnitsFromDb readUnitsFromDb = new ReadUnitsFromDb(this,IpAddress.read_lec_units_url);
        String unit_data="";
        ArrayList<UnitData> dataList2;

        try {
            unit_data = readUnitsFromDb.execute("STAFFID",STAFFID).get();
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
            adapter = new MyUnitsRecyclerAdapter(dataList2);
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
            Log.e("netback","json data: "+jsonArray.length());
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