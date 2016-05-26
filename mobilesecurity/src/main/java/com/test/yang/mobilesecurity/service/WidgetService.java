package com.test.yang.mobilesecurity.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.test.yang.mobilesecurity.R;
import com.test.yang.mobilesecurity.tools.TaskTools;
import com.test.yang.mobilesecurity.widget.MyWidget;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WidgetService extends Service {

    private AppWidgetManager appWidgetManager;
    private WidgetReceiver widgetReceiver;
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;
    private Timer timer;
    private SharedPreferences spConfig;

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        spConfig=getSharedPreferences("config",MODE_PRIVATE);
        appWidgetManager=AppWidgetManager.getInstance(this);
        updateWidget();
        //注册清理进程广播接受者
        //1.广播接受者
        widgetReceiver = new WidgetReceiver();
        //2.设置接受的广播事件
        IntentFilter widgetIntentFilter = new IntentFilter();
        widgetIntentFilter.addAction("mobilesecurity.WIDGET_CLEAR_ONCLICK");
        //3.注册广播接受者
        registerReceiver(widgetReceiver, widgetIntentFilter);

        //注册锁屏广播接受者
        screenOffReceiver=new ScreenOffReceiver();
        IntentFilter screenOffIntentFilter =new IntentFilter();

        screenOffIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(screenOffReceiver,screenOffIntentFilter);

        //注册解锁广播接受者
        screenOnReceiver=new ScreenOnReceiver();
        IntentFilter screenOnIntentFilter =new IntentFilter();
        screenOnIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenOnReceiver,screenOnIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdateWidget();
        //注销清理进程的广播接受者
        if (widgetReceiver != null) {
            unregisterReceiver(widgetReceiver);
            widgetReceiver = null;
        }
        if (screenOffReceiver != null) {
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver = null;
        }
        if (screenOnReceiver != null) {
            unregisterReceiver(screenOnReceiver);
            screenOnReceiver = null;
        }

    }

    /**
     * 停止更新桌面控件
     */
    private void stopUpdateWidget(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 更新桌面控件的方法
     */
    private void updateWidget(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer=new Timer();
                //task : 要执行操作
                //when : 延迟的时间
                //period : 每次执行的间隔时间
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d("WidgetService","桌面控件开始更新");
                        //获取远程组件的标示
                        ComponentName componentName=new ComponentName(WidgetService.this, MyWidget.class);
                        //获取远程布局
                        RemoteViews remoteViews=new RemoteViews(getPackageName(), R.layout.widget_desktop);
                        remoteViews.setTextViewText(R.id.process_count, "正在运行的进程数："+TaskTools.getProcessCount(WidgetService.this));
                        remoteViews.setTextViewText(R.id.process_memory,"可用内存："+ Formatter.formatFileSize(WidgetService.this,TaskTools.getAvailableRam(WidgetService.this)));

                        Intent clearClickIntent=new Intent();
                        clearClickIntent.setAction("mobilesecurity.WIDGET_CLEAR_ONCLICK");
                        PendingIntent clearClickPendingIntent=PendingIntent.getBroadcast(WidgetService.this,0,clearClickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        remoteViews.setOnClickPendingIntent(R.id.widget_clear_button,clearClickPendingIntent);

                        appWidgetManager.updateAppWidget(componentName,remoteViews);
                    }
                },2000,2000);
            }
        }).start();
    }

    /**
     * 创建桌面控件的广播接收者
     */
    private class WidgetReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WidgetReceiver","接收到桌面控件广播");
            killProgress();
        }
    }

    /**
     * 创建桌面控件锁屏广播接收者
     */
    private class ScreenOffReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            stopUpdateWidget();
            Log.d("ScreenOffReceiver","锁屏广播接收者");
            if(spConfig.getBoolean("lock_screen_clear",false)){
                killProgress();
            }
        }
    }

    /**
     * 创建桌面控件亮屏广播接收者
     */
    private class ScreenOnReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ScreenOnReceiver","亮屏广播接收者");
            updateWidget();
        }
    }

    private void killProgress() {
        Log.d("WidgetReceiver","桌面控件开始清理清理进程");
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获取正在运行进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            //判断我们的应用进程不能被清理
            if (!runningAppProcessInfo.processName.equals(getPackageName())) {
                activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
            }
        }
    }
}
