package com.test.yang.mobilesecurity.tools;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**服务状态工具类
 * Created by Administrator on 2016/5/16.
 */
public class RunningAddress {

    /**
     * 查询归属地服务是否运行
     * @param className 服务类名
     * @param context 上下文
     * @return 运行状态
     */
    public static Boolean isRunningAddressService(String className, Context context){
        //获取活动的管理者
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取当前所有运行活动信息的集合
        List<ActivityManager.RunningServiceInfo> runningServiceInfos=activityManager.getRunningServices(1000);
//        Log.d("RunningAddress",String.valueOf(runningServiceInfos.size()));
        //遍历集合
        for(ActivityManager.RunningServiceInfo runningServiceInfo:runningServiceInfos){
            //获取控件的标识
            ComponentName componentName=runningServiceInfo.service;
            //从标识中获取全类名
            String className2=componentName.getClassName();
//            Log.d("RunningAddress",className2);
            if(className.equals(className2)){
                return true;
            }
        }
        return false;
    }
}
