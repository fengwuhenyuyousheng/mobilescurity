package com.test.yang.photosafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.test.yang.photosafe.db.BlackNumberDBOperate;

public class CallSmsService extends Service {

    private BlackNumberDBOperate mBlackNumberDBOperate;
    private SmsReceiver mSmsReceiver;
    private TelephonyManager mTelephonyManager;
    private MyPhoneStateListener mMyPhoneStateListener;

    public CallSmsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBlackNumberDBOperate=new BlackNumberDBOperate(getApplicationContext());
        //代码注册短信到来的广播接受者
        //1.创建广播接受者
        mSmsReceiver = new SmsReceiver();
        //2.设置监听广播事件
        IntentFilter intentFilter = new IntentFilter();
        //广播接受者的最高优先级Integer.MAX_VALUE,优先级相同,代码注册的广播接受者先接受广播事件
        intentFilter.setPriority(1000);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //3.注册广播接受者
        registerReceiver(mSmsReceiver, intentFilter);

        //监听电话状态
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mMyPhoneStateListener = new MyPhoneStateListener();
        //参数1:监听
        //参数2:监听的事件
        mTelephonyManager.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
        //取消监听
        mTelephonyManager.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("代码注册广播接受者接受短信");
            //接受解析短信的操作
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs){
                //解析成SmsMessage
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();//获取短信的内容
                String sender = smsMessage.getOriginatingAddress();//获取发件人
                //根据发件人号码,获取号码的拦截模式
                int mode = mBlackNumberDBOperate.queryBlackNumberMode(sender);
                //判断是否是短信拦截或者是全部拦截
                if (mode == mBlackNumberDBOperate.MODE_SMS || mode == mBlackNumberDBOperate.MODE_ALL) {
                    abortBroadcast();//拦截广播事件,拦截短信操作
                }
            }
        }
    }


    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //如果是响铃状态,检测拦截模式是否是电话拦截,是挂断
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //获取拦截模式
                int mode = mBlackNumberDBOperate.queryBlackNumberMode(incomingNumber);
                if (mode == mBlackNumberDBOperate.MODE_CALL || mode == mBlackNumberDBOperate.MODE_ALL) {
                    //挂断电话 1.5
                    endCall();
                    //删除通话记录
                    //1.获取内容解析者
                    final ContentResolver resolver = getContentResolver();
                    //2.获取内容提供者地址  call_log   calls表的地址:calls
                    //3.获取执行操作路径
                    final Uri uri = Uri.parse("content://call_log/calls");
                    //4.删除操作
                    //resolver.delete(uri, "number=?", new String[]{incomingNumber});
                    //通过内容观察者观察内容提供者内容,如果变化,就去执行删除操作
                    //notifyForDescendents : 匹配规则,true : 精确匹配   false:模糊匹配
                    resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        //内容提供者内容变化的时候调用
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            //删除通话记录
                            resolver.delete(uri, "number=?", new String[]{incomingNumber});
                            //注销内容观察者
                            resolver.unregisterContentObserver(this);
                        }
                    });
                }
            }
        }
    }

    /**
     * 挂断电话的方法
     */
    private void endCall() {

    }
}
