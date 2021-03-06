package com.darajalab.TaiTrack;
/** The date variable here denotes the day a class happened
 * the value variable denotes Absent or Present for a class
 * **/
public class AttendanceData {

    private String date, value;
    //constructor
    public AttendanceData(String date,String value)
    {
        this.setDate(date);
        this.setValue(value);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
