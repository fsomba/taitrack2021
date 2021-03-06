package com.darajalab.TaiTrack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.concurrent.ExecutionException;

public class AddUnitsRecyclerAdapter extends RecyclerView.Adapter<AddUnitsRecyclerAdapter.RecyclerViewHolder> {
    ArrayList<UnitData> unitList;
    //constructor
    public AddUnitsRecyclerAdapter(ArrayList<UnitData> unitList) {
        this.unitList = unitList;
    }

    //following 3 methods must be implemented
    //#1 create the views
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //fill the parent component with the cardview layout specified
        //parent here refers to the RecylerView
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_add_units, parent, false);
        return new RecyclerViewHolder(view);
    }

    UnitData unitData;
    //#2 bind data to the views
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //cast carInfoList ArrayList into a CarData object to enable access by getter and setter methods
        unitData = unitList.get(position);
        //set text and add numbering
        String number = String.valueOf(position+1);
        holder.unitCodeHolder.setText(String.format("%s. %s", number, unitData.getUnit_code()));
        holder.unitNameHolder.setText(String.format("    %s", unitData.getUnit_name()));
    }

    //#3 get number of items in data set
    @Override
    public int getItemCount() {
        return unitList.size();
    }

    // custom inner class
    /**defines the type of view the recyler is working with for each data item e.g cardview*/
    //static class to reference views in custom layout
    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView unitCodeHolder,unitNameHolder;
        private Button btnAddUnit;
        private String response;
        private Context context;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //reference components in cardview layout
            unitCodeHolder = itemView.findViewById(R.id.txt_unit_code);
            unitNameHolder = itemView.findViewById(R.id.txt_unit_name);
            btnAddUnit = itemView.findViewById(R.id.btnAddUnit);
            btnAddUnit.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            //get the clicked object position
            int position = getAdapterPosition();
            UnitData clickedObject= unitList.get(position);
            final String unit_code = clickedObject.getUnit_code();
            final String unit_name = clickedObject.getUnit_name();
            final String REGNO = StudentHome.REGNO;

            //alert dialog to confirm unit addition
            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
            alertDialog.setTitle("Confirm Adding of: "+unit_code);
            //
            alertDialog.setMessage("Add "+ unit_name + " to your units?");

            //create and handle a positive response
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //push the unit to the database
                            try {
                                response = new PushUnitToDb().execute(REGNO,unit_code,unit_name).get();
                                if(response.startsWith("Unit Added")){
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
    }

    public static class PushUnitToDb extends AsyncTask<String, Void, String> {
        String server_response="";

        @Override
        protected String doInBackground(String... params) {
            String REGNO = params[0];
            String UNIT_CODE =params[1];
            String UNIT_NAME =params[2];

            try{
                URL url =new URL(IpAddress.push_unit_to_db_url);
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

                //read the php output on unit addition status
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
                server_response = "Addition failed!";
                e.printStackTrace();
            }
            return server_response;
        }
    }
}
