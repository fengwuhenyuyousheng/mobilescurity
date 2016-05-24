package com.test.yang.photosafe.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.test.yang.photosafe.model.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**这是进程工具类
 * Created by Administrator on 2016/5/24.
 */
public class TaskTools {

    /**
     * 获取所有进程信息
     * @param context
     * @return
     */
    public static List<TaskInfo> getAllTaskInfo(Context context){
        List<TaskInfo> taskLists=new ArrayList<TaskInfo>();
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager=context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList=activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo:runningAppProcessInfoList){
            TaskInfo taskInfo=new TaskInfo();
            taskInfo.setPackageName(runningAppProcessInfo.processName);
            Debug.MemoryInfo[] memoryInfo=activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
            int totalPss=memoryInfo[0].getTotalPss();
            long ramSize=totalPss*1024;
            taskInfo.setRamSize(ramSize);
            try {
                ApplicationInfo applicationInfo=packageManager.getApplicationInfo(taskInfo.getPackageName(),0);
                taskInfo.setIcon(applicationInfo.loadIcon(packageManager));
                taskInfo.setName(applicationInfo.loadLabel(packageManager).toString());
                int flags = applicationInfo.flags;
                boolean isUser;
                //判断是否是用户程序
                if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
                    //系统程序
                    isUser = false;
                }else{
                    //用户程序
                    isUser = true;
                }
                //保存信息
                taskInfo.setUser(isUser);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            taskLists.add(taskInfo);
        }
        return taskLists;
    }
}
