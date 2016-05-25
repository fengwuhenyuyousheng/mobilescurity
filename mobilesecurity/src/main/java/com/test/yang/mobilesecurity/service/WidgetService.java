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
    private Timer timer;

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appWidgetManager=AppWidgetManager.getInstance(this);
        updateWidget();
        //注册清理进程广播接受者
        //1.广播接受者
        widgetReceiver = new WidgetReceiver();
        //2.设置接受的广播事件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("mobilesecurity.WIDGET_CLEAR_ONCLICK");
        //3.注册广播接受者
        registerReceiver(widgetReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销清理进程的广播接受者
        if (widgetReceiver != null) {
            unregisterReceiver(widgetReceiver);
            widgetReceiver = null;
        }
        //停止更新widget
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
