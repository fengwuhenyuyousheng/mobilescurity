package com.test.yang.mobilesecurity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.test.yang.mobilesecurity.R;

/**这是手机防盗功能主界面
 * Created by Administrator on 2016/5/10.
 */
public class PhotoSafeActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private Button againSafeSet;
    private CheckBox safeLockCheck;
    private TextView safeLockText;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_safe);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("first_enter_safe",true)){
            Intent jumpSafeSettingOneActivity=new Intent(this,SafeSettingOneActivity.class);
            startActivity(jumpSafeSettingOneActivity);
            finish();
        }
        safeLockCheck= (CheckBox) findViewById(R.id.safe_lock_checkbox);
        safeLockText= (TextView) findViewById(R.id.safe_lock_text_view);
        if(sharedPreferences.getBoolean("safe_protect",false)){
            safeLockCheck.setChecked(true);
            safeLockText.setText("防盗保护已经开启");
        }else{
            safeLockCheck.setChecked(false);
            safeLockText.setText("防盗保护未开启");
        }
        againSafeSet= (Button) findViewById(R.id.again_safe_set_button);
        againSafeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpSafeSettingOneActivity=new Intent(PhotoSafeActivity.this,SafeSettingOneActivity.class);
                startActivity(jumpSafeSettingOneActivity);
                finish();
            }
        });
    }
}
