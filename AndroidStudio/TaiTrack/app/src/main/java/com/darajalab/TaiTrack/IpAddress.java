package com.darajalab.TaiTrack;

/* this class contains links to the wampserver scripts used by the application */
/* emulator ip 10.0.2.2 */
//laptop ip 192.168.43.136
//server 137.117.34.207

public class IpAddress {
    //declare variables
    static String server_ip = "http://192.168.43.136";
    //variable to hold location difference btn lecturer and student
    static double location_difference = 0.0;
    //variable to hold student reg no
    static String regno = "";
    //
    public static String student_register_url = server_ip + "/kasukupro/student_register.php";
    public static String student_login_url = server_ip + "/kasukupro/student_login.php";
    //
    public static String student_units_url = server_ip + "/kasukupro/read_myunits.php";
    public static String add_units_url = server_ip + "/kasukupro/add_unit.php";
    //
    public static String drop_unit_url = server_ip + "/kasukupro/drop_unit.php";
    public static String push_unit_to_db_url = server_ip + "/kasukupro/push_chosen_unit_to_db.php";
    //
    public static String unit_attendance_url = server_ip + "/kasukupro/read_unit_attendance.php";
    public static String sign_attendance_url = server_ip + "/kasukupro/sign_attendance.php";
    //
    public static String read_unit_location_url = server_ip + "/kasukupro/read_unit_location.php";
}
