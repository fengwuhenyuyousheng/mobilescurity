package com.test.yang.mobilesecurity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**这是新建数据库
 * Created by Administrator on 2016/5/18.
 */
public class BlackNumberDBHelper extends SQLiteOpenHelper{

    public static final String DB_TABLE_NAME="info";

    public BlackNumberDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
//        Log.d("BlackNumberDBHelper","创建数据库");
    }

//    public BlackNumberDBHelper(Context context){
//        super(context, "blacknum.db", null, 1);
//        Log.d("BlackNumberDBHelper","创建数据库");
//    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.d("BlackNumberDBHelper","创建表");
        db.execSQL("create table "+ DB_TABLE_NAME
                + " (_id integer primary key autoincrement, black_number varchar(20),mode varchar(2))");
//        Log.d("BlackNumberDBHelper","创建表完成");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
