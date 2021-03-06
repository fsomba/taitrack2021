package com.darajalab.TaiTrackLec;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyUnitsRecyclerAdapter extends RecyclerView.Adapter<MyUnitsRecyclerAdapter.RecyclerViewHolder> {
    ArrayList<UnitData> unitList;
    //constructor
    public MyUnitsRecyclerAdapter(ArrayList<UnitData> unitList) {
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
                .inflate(R.layout.cardview_myunits, parent, false);
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

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //reference components in cardview layout
            unitCodeHolder = itemView.findViewById(R.id.unit_code);
            unitNameHolder = itemView.findViewById(R.id.unit_name);
        }

        //react to clicks
        @Override
        public void onClick(View view) {
            //get the clicked object position
            int position = getAdapterPosition();
            UnitData clickedObject= unitList.get(position);

            Intent intent=new Intent(view.getContext(),UnitDetails.class);
            intent.putExtra("UNIT_CODE",clickedObject.getUnit_code());
            intent.putExtra("UNIT_NAME", clickedObject.getUnit_name());
            //open UnitDetails activity
            view.getContext().startActivity(intent);
        }
    }
}
