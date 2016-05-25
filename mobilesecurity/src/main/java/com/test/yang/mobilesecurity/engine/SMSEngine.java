package com.test.yang.mobilesecurity.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.test.yang.mobilesecurity.model.SMSInfo;

import java.util.ArrayList;
import java.util.List;

/**这是通过数据库获取到短信信息
 * Created by Administrator on 2016/5/11.
 */
public class SMSEngine {

    /**
     * 这是通过数据库查询收件箱的信息
     * @param context
     * @return 查询到的收件箱信息
     */
    public static List<SMSInfo> getAllSMSInfo(Context context){

        ArrayList<SMSInfo> arrayList=new ArrayList<SMSInfo>();
        //获取内容解析者
        ContentResolver contentResolver=context.getContentResolver();
        //获取内容提供者地址并生成查询地址
        Uri smsInbox=Uri.parse("content://sms");
        //根据地址查询表
        Cursor cursor=contentResolver.query(smsInbox,new String[]{"address","body"},null,null,null);
        Log.d("SMSEngine","结果长度:"+cursor.getCount());
        //解析Cursor
        while (cursor.moveToNext()){
            Log.d("SMSEngine","分析查到的结果");
            SMSInfo smsInfo=new SMSInfo();
            smsInfo.setSmsAddress(cursor.getString(cursor.getColumnIndex("address")));
            smsInfo.setSmsBody(cursor.getString(cursor.getColumnIndex("body")));
            arrayList.add(smsInfo);
        }
        cursor.close();
        return arrayList;


    }

}
