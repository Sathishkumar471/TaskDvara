package com.example.taskdvara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper {

    final Context myContext;

    private SQLiteDatabase db;

    private static final int DB_VERSION = 1;

    public static final String DB_NAME = "dvaradb.db";

    private static final String PhoneNumber = "phoneNumber";
    private static final String NetSpeed = "netSpeed";
    private static final String DateTime = "dateTime";

    private static final String TABLE_User = "User";

    public DBClass(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        System.out.println("----" + DB_VERSION);
    }

    public DBClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, Context myContext) {
        super(context, name, factory, version);
        this.myContext = myContext;
    }


    String CREATE_USER = "CREATE TABLE IF NOT EXISTS '" + TABLE_User + "' ('"
            + PhoneNumber + "' TEXT NOT NULL, '"
            + NetSpeed + "' TEXT NOT NULL, '"
            + DateTime + "' TEXT NOT NULL)";


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_USER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {

            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
            } catch (SQLException e) {
                Log.i("ALTER", "IsMandatory already exists");
            }

            try {
                db.execSQL(CREATE_USER);
            } catch (SQLException e) {
                Log.i("ALTER", "IsMandatory already exists");
            }
        }

    }

    public void Insert_User(String phoneNumber, String netSpeed, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(PhoneNumber, phoneNumber);
        values.put(NetSpeed, netSpeed);
        values.put(DateTime, dateTime);

        long ack = db.insert(TABLE_User, null, values);
        if (ack > 0) {
            Log.i(TABLE_User, "inserted");
        } else {
            Log.i(TABLE_User, "failed");
        }
    }

    public void openToWrite() {
        closeDB();
        db = this.getWritableDatabase();
        Log.i("DataBase", "Database Opened");
    }

    // Closing database
    public void closeDB() {
        db = this.getWritableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
            Log.i("DataBase", "Database Closed");
        }
    }

    public List<User> getUserDetails() {
        List<User> userdetails = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c;
        String brandQuery = "select * from User";
        c = database.rawQuery(brandQuery, null);
        User userDetail;
        if (c.moveToFirst()) {
            try {
                do {
                    userDetail = new User();
                    userDetail.setPhoneNumber(CheckString(c, PhoneNumber));
                    userDetail.setNetSpeed(CheckString(c, NetSpeed));
                    userDetail.setDateTime(CheckString(c, DateTime));
                    userdetails.add(userDetail);
                } while (c.moveToNext());
            } catch (Exception e) {
                Log.e("Database", e.getLocalizedMessage());
            }
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return userdetails;
    }

    public static String CheckString(Cursor c, String col) {
        String str = "";
        if (c.getString(c.getColumnIndex(col)) != null && c.getString(c.getColumnIndex(col)).length() > 0) {
            str = c.getString(c.getColumnIndex(col));
        }
        return str;
    }
}
