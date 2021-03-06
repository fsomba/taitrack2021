package com.darajalab.TaiTrackLec;

/* this class contains links to the wampserver scripts used by the application */
/* emulator ip 10.0.2.2 */
//laptop ip 192.168.43.136
//server 137.117.34.207

public class IpAddress {
    //declare variables
    static String server_ip = "http://192.168.43.136/";
    //
    public static String lec_register_url = server_ip + "kasukupro/lec_register.php";
    public static String lec_login_url = server_ip + "kasukupro/lec_login.php";
    //
    public static String student_attendance_url = server_ip + "kasukupro/student_attendance.php";
    public static String read_lec_units_url = server_ip + "kasukupro/read_lec_units.php";
    public static String lec_add_units_url = server_ip + "kasukupro/lec_add_unit.php";
    //
    public static String lec_drop_unit_url = server_ip + "kasukupro/lec_drop_unit.php";
    public static String lec_push_unit_to_db_url = server_ip + "kasukupro/lec_push_unit_to_db.php";
    //
    public static String turn_signing_on = server_ip + "kasukupro/turn_on_signing.php";
    public static String turn_signing_off = server_ip + "kasukupro/turn_off_signing.php";
    //
    public static String view_unit_attendance = server_ip + "kasukupro/view_unit_attendance.php";
}
