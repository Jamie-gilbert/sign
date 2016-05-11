package chek_ins.com.sign.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import chek_ins.com.sign.utils.L;

/**
 * Created by Administrator on 2016/4/5.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, "sign.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String  sql = "CREATE TABLE record ( id integer PRIMARY KEY autoincrement, userid VARCHAR(36),coursename VARCHAR(50),coursetime VARCHAR(50),sign VARCHAR(20),vacation VARCHAR(20),absence VARCHAR(20),room  VARCHAR(20),teacher  VARCHAR(20))";
        db.execSQL(sql);
        sql = "CREATE TABLE modifyrecord ( id integer PRIMARY KEY autoincrement, userid VARCHAR(36),coursename VARCHAR(50),coursetime VARCHAR(50),sign VARCHAR(20),vacation VARCHAR(20),absence VARCHAR(20),room  VARCHAR(20),position integer,teacher  VARCHAR(20) )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE record";
        db.execSQL(sql);
         sql = "CREATE TABLE record ( id integer PRIMARY KEY autoincrement, userid VARCHAR(36),coursename VARCHAR(50),coursetime VARCHAR(50),sign VARCHAR(20),vacation VARCHAR(20),absence VARCHAR(20),room  VARCHAR(20),teacher  VARCHAR(20))";
        db.execSQL(sql);
//        sql = "DROP TABLE modifyrecord";
//        db.execSQL(sql);
         sql = "CREATE TABLE modifyrecord ( id integer PRIMARY KEY autoincrement, userid VARCHAR(36),coursename VARCHAR(50),coursetime VARCHAR(50),sign VARCHAR(20),vacation VARCHAR(20),absence VARCHAR(20),room  VARCHAR(20),position integer,teacher  VARCHAR(20) )";
        db.execSQL(sql);
        L.log("update222222222");
    }
}
