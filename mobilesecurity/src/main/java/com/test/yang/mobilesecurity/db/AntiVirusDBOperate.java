package com.test.yang.mobilesecurity.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**这是病毒数据库操作
 * Created by Administrator on 2016/5/28.
 */
public class AntiVirusDBOperate {

    public static String ANTIVIRUS_BD_NAME="antivirus.db";
    public static String ANTIVIRUS_TABLE_NAME="datable";

    /**
     * 根据输入的号码查询数据库
     * @param signature 输入查询的号码
     * @param context 输入上下文
     * @return 返回查询的结果
     */
    public static boolean queryAddress(String signature,Context context){
        boolean isHave=false;
        //获取数据库的路径
        File file=new File(context.getFilesDir(),ANTIVIRUS_BD_NAME);
        //根据路径打开数据库文件
        SQLiteDatabase sqLiteDatabase=SQLiteDatabase.openDatabase(file.getAbsolutePath(),null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.query(ANTIVIRUS_TABLE_NAME,null,"md5 =?",new String[]{signature},null,null,null,null);
        while(cursor.moveToNext()){
            isHave=true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return isHave;
    }
}
