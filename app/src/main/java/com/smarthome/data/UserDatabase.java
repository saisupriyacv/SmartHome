package com.smarthome.data;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class UserDatabase {
    private static final String DATABASE_NAME = "HomeSecurity.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HOMEAUTO = "HomeAutomationDatabase";
    private static final String COL_USERNAME = "USEERNAME";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_SECURITYMODE = "SECURITYMODE";
    private static final String COL_DOORSTATUS = "DOORSTATUS";
    private static final String COL_BULBSTATUS = "BULBSTATUS";
    private static final String COL_SWITCHSTATUS = "SWITCHSTATUS";
    private static final String COL_AUTOLOGIN = "AUTOLOGIN";
    private static final String COL_PASSCODE = "PASSCODE";

    private static final String COL_ID = "ID ";
    private static String AutoLogin;
    private static String SignOut;


    private String ID;
    private String User_name;
    private String Password;
    private String Email;
    private String Security_Mode;
    private String Door_Status;
    private String Bulb_Status;
    private String Switch_Status;
    private Context context;
    Database_helper helper;
    public  SQLiteDatabase db;
    private String msg = "user database";
    private String Passcode;


    public UserDatabase(Context _context){
        context = _context;
        Log.d(msg, ":constructor callled");

        helper = new Database_helper(context,DATABASE_NAME,null,DATABASE_VERSION);


    }
    public UserDatabase open() throws SQLException
    {
        Log.d(msg, ":Open is callled");
        db = helper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        Log.d(msg, "SQLiteDatabase is calledddddddddd");
        return db;
    }

    private static final String DATABASE_Homecontrol = "create table " + TABLE_HOMEAUTO +"( "
            + COL_ID +" integer primary key autoincrement,"+  COL_USERNAME + " text,"
            + COL_PASSWORD + " text," + COL_EMAIL +" text,"+ COL_SECURITYMODE + " text," +
            COL_DOORSTATUS + " text," + COL_SWITCHSTATUS + " text," + COL_BULBSTATUS +" text," + COL_PASSCODE +" text," + COL_AUTOLOGIN +" text" +"); ";


    public void debugTables(){

        //getDatabaseStructure();
        // boolean a = helper.isFieldExist(table_User,col_UserName);
    }

    public String getUserName(){ return User_name; }
    public String getID(){
        return ID;
    }
    public String getPassword(String user){

        return getSingleValue(COL_PASSWORD,user,"Password");
    }
    public String getEmail(){

        Email = getSingleValue(COL_EMAIL,getUserName(),"Email");
        return Email;
    }
    public String getSecurityMode(){
        return getSingleValue(COL_SECURITYMODE,getUserName(),"SecurityMode");
    }
    public String getDoor_Status(){
        return getSingleValue(COL_DOORSTATUS,getUserName(),"DoorStatus");
    }
    public String getBulbStatus(){
        return getSingleValue(COL_BULBSTATUS,getUserName(),"BulbStatus");
    }
    public String getSwitchStatus(){
        return getSingleValue(COL_SWITCHSTATUS,getUserName(),"SwitchStatus");
    }

    public String getAutoLogin(){
        AutoLogin = getSingleValue(COL_AUTOLOGIN,getUserName(),"AutoLogin");
        return AutoLogin;
    }
    public String getLogOff(){
        return SignOut;
    }

    public static String getDatabaseStringHome() {

        return DATABASE_Homecontrol;
    }


    public void setID(String id){
        ID = id;
    }
    public void setUserName(String userName){
        User_name = userName;

    }
    public void setPassword(String password){
        Password = password;
    }
    public void setEmail(String email){
        Email = email;
    }
    public void setSecurityMode(String security_mode){
        Security_Mode = security_mode;
        UpdateValues(COL_SECURITYMODE,getUserName(),Security_Mode);
    }
    public void setDoorStatus(String door_status){
        Door_Status = door_status;
        UpdateValues(COL_DOORSTATUS,getUserName(),Door_Status);

    }
    public void setBulbStatus(String bulb_status){
        Bulb_Status = bulb_status;
        UpdateValues(COL_BULBSTATUS,getUserName(),Bulb_Status);
    }
    public void setSwitchStatus(String switch_status){
        Switch_Status = switch_status;
        UpdateValues(COL_SWITCHSTATUS,getUserName(),Switch_Status);

    }
    public void setAutoLogin(String autoLogin){
        AutoLogin= autoLogin;
        UpdateValues(COL_AUTOLOGIN,getUserName(),AutoLogin);
    }
    public void setLogOff(String singout){
        SignOut= singout;
    }


    public String getPasscode() {
        return Passcode;

    }

    public void setPasscode(String passcode) {
        Passcode = passcode;
        UpdateValues(COL_PASSCODE,getUserName(),Passcode);
    }


    public void StoreValues(String value_user,String value_pass, String value_email) {

        Log.d(msg, ":StoreValues called");

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, value_user);
        values.put(COL_PASSWORD, value_pass);
        values.put(COL_EMAIL, value_email);
        values.put(COL_SECURITYMODE, "default_value");
        values.put(COL_DOORSTATUS, "default_value");
        values.put(COL_SWITCHSTATUS, "default_value");
        values.put(COL_BULBSTATUS, "default_value");
        values.put(COL_AUTOLOGIN,"default value");
        values.put(COL_PASSCODE,"default value");

        db.insertOrThrow(TABLE_HOMEAUTO, null, values);
        setEmail(value_email);
        setUserName(value_user);
        setPassword(value_pass);

    }
    public void UpdateValues(String column_name, String user_name ,String column_values){

        Log.d(msg, ":Updated called");
        ContentValues valuess = new ContentValues();
        valuess.put(column_name, column_values);
        String combine = COL_USERNAME + "=?";
        db.update(TABLE_HOMEAUTO, valuess, combine, new String[]{String.valueOf(user_name)});

    }
    public String getSingleValue( String column_name,String userName, String return_option){
        String Singlevalue= null;
        String combine = COL_USERNAME + "=?";
        Cursor cursor_single = db.query(TABLE_HOMEAUTO,null,combine,new String[]{userName},null,null,null);

        if(cursor_single.getCount()<1) // UserName Not Exist
        {
            Singlevalue = "NOT EXIST";
        }
        else
        {
            cursor_single.moveToFirst();
            switch (return_option) {
                case "Password":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_PASSWORD));
                    break;
                case "Email":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_EMAIL));

                    break;
                case "SecurityMode":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_SECURITYMODE));
                    break;
                case "DoorStaatus":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_DOORSTATUS));
                    break;
                case "SwitchStatus":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_SWITCHSTATUS));

                    break;
                case "BulbStatus":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_BULBSTATUS));
                    break;
                case "AutoLogin":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_AUTOLOGIN));
                    break;

                case "Passcode":
                    Singlevalue = cursor_single.getString(cursor_single.getColumnIndex(COL_AUTOLOGIN));
                    break;
            }

        }
        cursor_single.close();

        return Singlevalue;
    }


}


