package com.test.yang.mobilesecurity.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.test.yang.mobilesecurity.model.TaskInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    /**
     * 获取正在运行的进程的个数
     * @return
     */
    public static int getProcessCount(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return runningAppProcesses.size();
    }
    /**
     * 获取剩余内存
     * @return
     */
    public static long getAvailableRam(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取内存的信息,保存到MemoryInfo中
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        //获取空闲的内存
        //outInfo.availMem;
        //		//获取总的内存
        //		outInfo.totalMem;
        return outInfo.availMem;
    }

    /**
     * 获取总的内存
     * @return
     * @deprecated
     */
//    public static long getTotalRam(Context context){
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        //获取内存的信息,保存到MemoryInfo中
//        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
//        activityManager.getMemoryInfo(outInfo);
//        //获取空闲的内存
//        //outInfo.availMem;
//        //		//获取总的内存
//        //		outInfo.totalMem;
//        return outInfo.totalMem;//16版本之上才有,之下是没有的
//    }
    /**
     * 兼容低版本
     * @return
     */
    public static long getTotalRam(){
        File file = new File("/proc/meminfo");
        StringBuilder sb = new StringBuilder();
        try {
            //读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = br.readLine();
            //获取数字
            char[] charArray = readLine.toCharArray();
            for (char c : charArray) {
                if (c>='0' && c<='9') {
                    sb.append(c);
                }
            }
            String string = sb.toString();
            //转化成long
            long parseLong = Long.parseLong(string);
            return parseLong*1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
