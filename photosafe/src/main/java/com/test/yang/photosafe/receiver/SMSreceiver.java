package com.test.yang.photosafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.service.GPSService;

/**这是短信广播接收者
 * Created by Administrator on 2016/5/11.
 */
public class SMSReceiver extends BroadcastReceiver {

    private static MediaPlayer mediaPlayer;

//    private ArrayList<SMSInfo> arrayList;
    @Override
    public void onReceive(Context context, Intent intent) {

        DevicePolicyManager devicePolicyManager= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName=new ComponentName(context,Admin.class);
        /*
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
        */
        //接受解析短信
        //70汉字一条短信,71汉字两条短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for(Object obj:objs){
            //解析成SmsMessage
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();//获取短信的内容
            String sender = smsMessage.getOriginatingAddress();//获取发件人
            System.out.println("发件人:"+sender+"  短信内容:"+body);
            //真机测试,加发件人判断
            //判断短信是哪个指令
            if ("#*location*#".equals(body)) {
                //GPS追踪
                System.out.println("GPS追踪");
                //开启一个服务
                Intent intent_gps = new Intent(context,GPSService.class);
                context.startService(intent_gps);
                //拦截短信
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*alarm*#".equals(body)){
                //在播放报警音乐之前,将系统音量设置成最大
                //声音的管理者
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                //设置系统音量的大小
                //streamType : 声音的类型
                //index : 声音的大小   0最小    15最大
                //flags : 指定信息的标签
                //getStreamMaxVolume : 获取系统最大音量,streamType:声音的类型
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                //判断是否在播放报警音乐
                if (mediaPlayer!=null) {
                    mediaPlayer.release();//释放资源
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.love_you);
                //mediaPlayer.setVolume(1.0f, 1.0f);//设置最大音量,音量比例
                //mediaPlayer.setLooping(true);
                mediaPlayer.start();
                //播放报警音乐
                System.out.println("播放报警音乐");
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*wipedata*#".equals(body)){
                if (devicePolicyManager.isAdminActive(componentName)) {
                    //远程删除数据
                    devicePolicyManager.wipeData(0);
                    System.out.println("远程删除数据");
                }
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*lockscreen*#".equals(body)){
                if(devicePolicyManager.isAdminActive(componentName)){
                    //远程锁屏
                    devicePolicyManager.lockNow();
                    System.out.println("远程锁屏");
                }
//                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }

        }
    }
}
