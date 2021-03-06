package com.darajalab.TaiTrackLec;

/** The present variable here denotes number of times a student has been present
 * the absent variable denotes Absent number of times a student has been absent
 * **/
public class AttendanceData {

    private String regno, student_name, present, absent;
    //constructor
    public AttendanceData(String regno, String student_name, String present, String absent)
    {
        this.regno = regno;
        this.student_name = student_name;
        this.present=present;
        this.absent=absent;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getRegno() {
        return regno;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getPresent() {
        return present;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getAbsent() {
        return absent;
    }
}
