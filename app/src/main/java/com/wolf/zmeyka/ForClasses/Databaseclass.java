package com.wolf.zmeyka.ForClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.wolf.zmeyka.Model.Record;

import java.util.ArrayList;

public class Databaseclass extends SQLiteOpenHelper {


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + "RecordsInfo" + " (" +
                    "ID" + " INTEGER PRIMARY KEY, " +
                    "NAME" + " TEXT, " +
                    "RECORD" + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + "RecordsInfo";


    public Databaseclass(Context context) {
        super(context, "Records.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( SQL_CREATE_ENTRIES );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL( SQL_DELETE_ENTRIES );
        onCreate( db );
    }

    public long insertRecord(Record records){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", records.getName());
        contentValues.put("RECORD", records.getRecord());

        long result = db.insert("RecordsInfo", null, contentValues);

        return result;
    }


    public ArrayList<Record> getAllData()
    {
     ArrayList<Record> arrayList = new ArrayList<>();
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery("SELECT * FROM RecordsInfo", null);

     while (cursor.moveToNext())
     {
         int id = cursor.getInt(0);
         String name = cursor.getString(1);
         int record = cursor.getInt(2);
         Record record1 = new Record(id, record, name);

         arrayList.add(record1);

     }

     return arrayList;
    }
}
