package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.test.yang.photosafe.R;

/**这是手机防盗设置界面一
 * Created by Administrator on 2016/5/9.
 */
public class SafeSettingOneActivity extends SafeSettingBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting_one);

    }

    @Override
    public void nextActivitySubClass() {
        Intent nextActivity=new Intent(this,SafeSettingTwoActivity.class);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void previousActivitySubClass() {

    }
}
