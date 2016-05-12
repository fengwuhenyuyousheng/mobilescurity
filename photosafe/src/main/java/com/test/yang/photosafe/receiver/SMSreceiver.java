package com.test.yang.photosafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.test.yang.photosafe.engine.SMSEngine;
import com.test.yang.photosafe.model.SMSInfo;

import java.util.ArrayList;

/**这是短信广播接收者
 * Created by Administrator on 2016/5/11.
 */
public class SMSReceiver extends BroadcastReceiver {

    private ArrayList<SMSInfo> arrayList;
    @Override
    public void onReceive(Context context, Intent intent) {
        arrayList= (ArrayList<SMSInfo>) SMSEngine.getAllSMSInfo(context);
        for(SMSInfo smsInfo:arrayList){
            String address=smsInfo.getSmsAddress();
            String body=smsInfo.getSmsBody();
            System.out.println("发件人:"+address+"  短信内容:"+body);
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
        }

    }
}
