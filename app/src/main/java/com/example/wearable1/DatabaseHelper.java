package com.example.wearable1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String ID = "id";
    public static final String DATE = "Date";
    public static final String TYPE = "Type";
    public static final String TIME = "Time";
    public static final String TEST = "test";
    public static final String DATA = "Data";

    public DatabaseHelper(Context context) {
        super(context, "Result.db", null, 1);
    }

    //Create local table
    @Override
    public void onCreate(SQLiteDatabase Db) {
        Db.execSQL("create Table " + TEST + "(id INTEGER PRIMARY KEY AUTOINCREMENT,Date Text,Time Text, Type Test,Result String,Data Integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase Db, int i, int i1) {
        Db.execSQL("Drop Table if exists " + TEST);
    }

    //Store the symptom type, date and time locally
    @SuppressLint("Range")
    public boolean insertData(String Date, String Type, String Time, int data){
        System.out.println("hmm 1?");
        SQLiteDatabase Db = this.getWritableDatabase();
        System.out.println("22");
        ContentValues contentValues = new ContentValues();
        System.out.println("33");
        contentValues.put(DATE,Date);
        System.out.println("44");
        contentValues.put(TIME,Time);
        System.out.println("55");
        contentValues.put(TYPE,Type);
        System.out.println("66");
        contentValues.put(DATA,data);
        System.out.println("77");
        //Cursor cursor = Db.rawQuery("SELECT id FROM " + TEST, null);
        //cursor.moveToLast();

        /*if (cursor.getCount() > 0) {
            if (checkTime(cursor.getString(cursor.getColumnIndex(TIME)), Time) != 0) {
                data = cursor.getInt(cursor.getColumnIndex(DATA));
                return updateData(Date, Type, Time, data++);
            }
        }*/

        long inserted = Db.insert(TEST,null,contentValues);
        if(inserted==-1){
            return false;
        }else {
            return true;
        }
    }

    public int checkTime(String t1, String t2){
        int h1 = new Integer(t1.substring(3,5));
        int h2 = new Integer(t2.substring(3,5));

        return h1 - h2;
    }


    public boolean updateData(String Date,String Type,String Time, int data){
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        contentValues.put(DATE,Date);
        contentValues.put(TIME,Time);
        contentValues.put(TYPE,Type);
        contentValues.put(DATA,data);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.

        long inserted = db.update(TEST, contentValues, "date=? AND time=? & type=?", new String[]{Date,Time,Type});
        if(inserted==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getLastEntry(){
        SQLiteDatabase Db = this.getWritableDatabase();
        Cursor cursor = Db.rawQuery("SELECT id FROM " + TEST, null);
        cursor.moveToLast();

        return cursor;
    }



    @Override
    public synchronized void close() {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null){
            db.close();
            super.close();
        }

    }
}
