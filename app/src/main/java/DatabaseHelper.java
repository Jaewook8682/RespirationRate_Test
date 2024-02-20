package no.nordicsemi.android.blinky;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import no.nordicsemi.android.blinky.utils.GraphData;

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
        SQLiteDatabase Db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE,Date);
        contentValues.put(TIME,Time);
        contentValues.put(TYPE,Type);
        contentValues.put(DATA,data);
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

    @SuppressLint("Range")
    public LineGraphSeries<DataPoint> getGraphArray(){
        SQLiteDatabase Db = this.getWritableDatabase();
        LineGraphSeries<DataPoint> data = new LineGraphSeries<>(new DataPoint[]{});
        String sql = "SELECT id FROM test WHERE Date >= ?";
        //Cursor cursor = Db.rawQuery(sql, new String[] {"0"});
        //cursor.moveToFirst();
        Cursor cursor = Db.rawQuery("SELECT * FROM " + TEST, null);
        while (cursor.moveToNext()){
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(TIME));
            Double x = new Double(time.substring(0,2)) +
                    new Double(time.substring(3,5))/100;
            if(data.getHighestValueX() != x) {
                data.appendData(new DataPoint(x, cursor.getInt(cursor.getColumnIndex(DATA))), true, 600);
            }
        }
        cursor.close();
        return data;
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
