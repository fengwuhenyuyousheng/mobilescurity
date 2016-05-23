package com.test.yang.photosafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.test.yang.photosafe.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**这是软件信息获取类
 * Created by Administrator on 2016/5/20.
 */
public class SoftWareInfoEngine {

    public static List<AppInfo> getAppInfo(Context context){
        List<AppInfo> applicationInfoList=new ArrayList<AppInfo>();
        PackageManager packageManager=context.getPackageManager();
        List<android.content.pm.PackageInfo> packageInfoList= packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo:packageInfoList){
            AppInfo appInfo=new AppInfo();
            appInfo.setPackageName(packageInfo.packageName);
            appInfo.setVersionName(packageInfo.versionName);
            ApplicationInfo applicationInfo=packageInfo.applicationInfo;
            appInfo.setAppName(applicationInfo.loadLabel(packageManager).toString());
            appInfo.setAppIcon(applicationInfo.loadIcon(packageManager));
            int flags=applicationInfo.flags;
            if((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM){
                appInfo.setIsUser(false);
            }else{
                appInfo.setIsUser(true);
            }
            if((applicationInfo.FLAG_EXTERNAL_STORAGE & flags)==applicationInfo.FLAG_EXTERNAL_STORAGE){
                appInfo.setIsSDCard(true);
            }else{
                appInfo.setIsSDCard(false);
            }
//            Log.d("SoftWareInfoEngine",appInfo.toString());
            applicationInfoList.add(appInfo);
        }
        return applicationInfoList;
    }
}
