package com.example.LiftsTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "lifts.db";
    public static final String TABLE_NAME = "Lifts_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "BENCH";
    public static final String COL_3 = "SQUAT";
    public static final String COL_4 = "DEADLIFT";//makes strings for values of db


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT,BENCH TEXT,SQUAT TEXT,DEADLIFT INTEGER)");//creates db

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {//updates table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String bench, String squat, String deadlift){//inserts data into db table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,bench);
        contentValues.put(COL_3,squat);
        contentValues.put(COL_4,deadlift);
        long result = db.insert(TABLE_NAME,null , contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME,null);//gathers data from db table to view
        return res;

    }

    public boolean updateData(String id, String bench, String squat, String deadlift){//updates db values
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,bench);
        contentValues.put(COL_3,squat);
        contentValues.put(COL_4,deadlift);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[]{ id });
        return true;


    }

}
