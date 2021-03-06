package com.test.yang.mobilesecurity.engine;

import android.test.AndroidTestCase;
import android.util.Log;

import com.test.yang.mobilesecurity.model.SMSInfo;

import java.util.ArrayList;

/** 这是数据库读取短信测试类
 * Created by Administrator on 2016/5/12.
 */
public class SMSEngineTest extends AndroidTestCase {

    public void testGetAllSMSInfo() throws Exception {

        ArrayList<SMSInfo> arrayList= (ArrayList<SMSInfo>) SMSEngine.getAllSMSInfo(getContext());
        Log.d("SMSEngineTest","短信数量:"+arrayList.size());
        for(SMSInfo smsInfo:arrayList){
            String address=smsInfo.getSmsAddress();
            String body=smsInfo.getSmsBody();
            Log.d("SMSEngineTest","发件人:"+address+"  短信内容:"+body);
            /*
            //判断短信是哪个指令
            if ("#*location*#".equals(body)) {
                //GPS追踪
                System.out.println("GPS追踪");
                //拦截短信
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*alarm*#".equals(body)){
                //播放报警音乐
                System.out.println("播放报警音乐");
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*wipedata*#".equals(body)){
                //远程删除数据
                System.out.println("远程删除数据");
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*lockscreen*#".equals(body)){
                //远程锁屏
                System.out.println("远程锁屏");
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }
            */
        }


    }
}