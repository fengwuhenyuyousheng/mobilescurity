package com.test.yang.photosafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yang.photosafe.R;

/**这是设置选项的自定义控件
 * Created by Administrator on 2016/5/6.
 */
public class SettingOptionsItem extends RelativeLayout{


    private TextView settingTitle;
    private TextView settingTextDescription;
    private CheckBox settingCheckBox;
    private String setDescriptionOn;
    private String setDescriptionOff;


    public SettingOptionsItem(Context context) {
        super(context);
        initialize();
    }

    public SettingOptionsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        String setTitle=attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","set_title");
        setDescriptionOn=attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","set_description_on");
        setDescriptionOff=attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","set_description_off");
        settingTitle.setText(setTitle);
        if(getSettingCheckBoxisCheck()){
            settingTextDescription.setText(setDescriptionOn);
            settingCheckBox.setChecked(true);
        }else{
            settingTextDescription.setText(setDescriptionOff);
            settingCheckBox.setChecked(false);
        }
    }

    public SettingOptionsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * 初始化自定义控件
     */
    public void initialize(){
        View view=View.inflate(getContext(),R.layout.item_setting_center,this);
        settingTitle = (TextView) view.findViewById(R.id.setting_title);
        settingTextDescription= (TextView) view.findViewById(R.id.setting_description);
        settingCheckBox= (CheckBox) view.findViewById(R.id.setting_checkbox);

    }

    /**
     * 设置选项标题
     * @param setTitle 选项标题
     */
    public void setSettingTitle(String setTitle){
        this.settingTitle.setText(setTitle);
    }

    /**
     * 设置选项的内容
     * @param setTextDescription 选项内容
     */
    public void setSettingDescription(String setTextDescription){
        this.settingTextDescription.setText(setTextDescription);
    }

    /**
     * 设置选项的复选框
     * @param isgCheck 复选框状态
     */
    public void setSettingCheckBox(Boolean isgCheck){
        if(isgCheck) {
            settingCheckBox.setChecked(true);
            settingTextDescription.setText(setDescriptionOn);
        }else{
            settingCheckBox.setChecked(false);
            settingTextDescription.setText(setDescriptionOff);
        }
    }

    /**
     * 查询选项复选框的状态
     * @return 复选框的状态
     */
    public Boolean getSettingCheckBoxisCheck(){
        return this.settingCheckBox.isChecked();
    }

}
