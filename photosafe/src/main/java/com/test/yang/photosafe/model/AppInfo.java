package com.test.yang.photosafe.model;

import android.graphics.drawable.Drawable;

/**这是应用程序信息类
 * Created by Administrator on 2016/5/20.
 */
public class AppInfo {

    private String mAppName;
    private String mPackageName;
    private String mVersionName;
    private Drawable mAppIcon;
    private Boolean isUser;
    private Boolean isSDCard;

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        this.mAppName = appName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public void setVersionName(String versionName) {
        this.mVersionName = versionName;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.mAppIcon = appIcon;
    }

    public Boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(Boolean isUser) {
        this.isUser = isUser;
    }

    public Boolean getIsSDCard() {
        return isSDCard;
    }

    public void setIsSDCard(Boolean isSDCard) {
        this.isSDCard = isSDCard;
    }

    public String toString(){
        return mAppName+"---"+mPackageName+"----"+mVersionName;
    }
}
