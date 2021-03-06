package com.darajalab.TaiTrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceRecyclerAdapter extends RecyclerView.Adapter<AttendanceRecyclerAdapter.RecyclerViewHolder> {
    ArrayList<AttendanceData> attendanceList;
    //constructor
    public AttendanceRecyclerAdapter(ArrayList<AttendanceData> attendanceList) {
        this.attendanceList = attendanceList;
    }

    //following 3 methods must be implemented
    //#1 create the views
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //fill the parent component with the cardview layout specified
        //parent here refers to the RecylerView
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_attendance, parent, false);
        return new RecyclerViewHolder(view);
    }

    AttendanceData attendanceData;
    //#2 bind data to the views
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //cast carInfoList ArrayList into a CarData object to enable access by getter and setter methods
        attendanceData = attendanceList.get(position);
        //set text and add numbering
        String number = String.valueOf(position+1);
        holder.dateHolder.setText(String.format("%s. %s", number, attendanceData.getDate()));
        holder.valueHolder.setText(String.format("    %s", attendanceData.getValue()));
    }

    //#3 get number of items in data set
    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    // custom inner class
    /**defines the type of view the recyler is working with for each data item e.g cardview*/
    //static class to reference views in custom layout
    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateHolder,valueHolder;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //reference components in cardview layout
            dateHolder = itemView.findViewById(R.id.txt_date);
            valueHolder = itemView.findViewById(R.id.txt_value);
        }

        //react to clicks
        @Override
        public void onClick(View view) {
            //get the clicked object position
            int position = getAdapterPosition();
            AttendanceData clickedObject= attendanceList.get(position);
            
        }
    }
}

