package com.example.vaio.chattylan;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper{
    static final String DATABASE_NAME="Logs.db";
    final String TAG ="tag";
    static final String TABLE_NAME="LocalHost_Messenging";
    static final String COLOMN_ID="Serial_No";
    static final String COLOMN_FLAG="Flag";
    static final String COLOMN_PARTY_IP="Party_IP";
    static final String COLOMN_MSG="Messages";
    static final String COLOMN_TIME="Time";

    public DBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, 1);
            }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+"("+COLOMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLOMN_FLAG+" INTEGER,"+COLOMN_TIME+" TEXT,"+COLOMN_PARTY_IP+" TEXT,"+COLOMN_MSG+" TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String> recieveQuery(String ip){

        SQLiteDatabase db = this.getWritableDatabase();

        String[] col ={COLOMN_PARTY_IP,COLOMN_MSG,COLOMN_TIME} ;
        Cursor cursor=db.query(TABLE_NAME, col ,COLOMN_PARTY_IP+" = '"+ip+"' AND "+COLOMN_FLAG+" =0",null,null,null,null);

        Log.i(TAG,"YO");
        cursor.moveToFirst();
        ArrayList<String> arrayList = new ArrayList<>();

        while(!cursor.isAfterLast()){

            arrayList.add(cursor.getString(cursor.getColumnIndex(COLOMN_PARTY_IP))+":"+
                          cursor.getString(cursor.getColumnIndex(COLOMN_TIME))+": "+"\n"+
                          cursor.getString(cursor.getColumnIndex(COLOMN_MSG)));

            cursor.moveToNext();
        }

        cursor.close();
        Log.i(TAG,"I am in recieveQuery()");
        return arrayList;
    }


    public ArrayList<String> sentQuery(String ip){

        SQLiteDatabase db = this.getWritableDatabase();

        String[] col ={COLOMN_PARTY_IP,COLOMN_MSG,COLOMN_TIME} ;
        Cursor cursor=db.query(TABLE_NAME, col ,COLOMN_PARTY_IP+" = '"+ip+"' AND "+COLOMN_FLAG+" =1",null,null,null,null);

        ArrayList<String> arrayList = new ArrayList<>();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex(COLOMN_PARTY_IP))+":"+
                          cursor.getString(cursor.getColumnIndex(COLOMN_TIME))+": "+"\n"+
                          cursor.getString(cursor.getColumnIndex(COLOMN_MSG)));

            cursor.moveToNext();
        }

        cursor.close();
        Log.i(TAG,"I am in sentQuery()");
        return arrayList;
    }


    public void addRow(String ip,int flag,String msg,String time){
        ContentValues contentValues =new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues.put(COLOMN_FLAG,flag);
        contentValues.put(COLOMN_MSG,msg);
        contentValues.put(COLOMN_PARTY_IP,ip);
        contentValues.put(COLOMN_TIME,time);

        db.insert(TABLE_NAME,null,contentValues);

        db.close();
    }

    public String printDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        String dbString="";
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE 1";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            dbString += cursor.getString(cursor.getColumnIndex(COLOMN_PARTY_IP));
            dbString +="  "+cursor.getString(cursor.getColumnIndex(COLOMN_FLAG));
            dbString +="  "+cursor.getString(cursor.getColumnIndex(COLOMN_MSG));
            dbString +="\n";
            cursor.moveToNext();
        }

        db.close();
        return dbString;
    }

}
