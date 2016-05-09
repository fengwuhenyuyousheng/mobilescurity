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


    private TextView settingText;
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
        settingText.setText(setTitle);
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
        View view=View.inflate(getContext(),R.layout.setting_center_item,this);
        settingText= (TextView) view.findViewById(R.id.setting_text);
        settingTextDescription= (TextView) view.findViewById(R.id.setting_description);
        settingCheckBox= (CheckBox) view.findViewById(R.id.setting_checkbox);

    }

    /**
     * 设置选项标题
     * @param settingTitle
     */
    public void setSettingTitle(String settingTitle){
        settingText.setText(settingTitle);
    }

    /**
     * 设置选项的内容
     * @param settingDescription
     */
    public void setSettingDescription(String settingDescription){
        settingTextDescription.setText(settingDescription);
    }

    /**
     * 设置选项的复选框
     * @param isgCheck
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
        return settingCheckBox.isChecked();
    }

}
