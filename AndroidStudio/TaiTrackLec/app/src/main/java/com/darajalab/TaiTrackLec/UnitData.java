package com.darajalab.TaiTrackLec;

public class UnitData {
    private String unit_code, unit_name;
    //constructor
    public UnitData(String unit_code,String unit_name)
    {
        this.setUnit_code(unit_code);
        this.setUnit_name(unit_name);
    }

    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }

    public String getUnit_code() {
        return unit_code;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUnit_name() {
        return unit_name;
    }
}
