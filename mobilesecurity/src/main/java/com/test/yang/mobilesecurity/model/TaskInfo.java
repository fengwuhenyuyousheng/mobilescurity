package com.test.yang.mobilesecurity.model;

import android.graphics.drawable.Drawable;

/**这是进程信息模型类
 * Created by Administrator on 2016/5/24.
 */
public class TaskInfo {

    //名称
    private String name;
    //图标
    private Drawable icon;
    //占用的内存空间
    private long ramSize;
    //包名
    private String packageName;
    //是否是用户程序
    private boolean isUser;
    //checkbox是否被选中
    private boolean isChecked;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
