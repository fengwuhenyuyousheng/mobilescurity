package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.test.yang.photosafe.R;

/**这是手机防盗功能主界面
 * Created by Administrator on 2016/5/10.
 */
public class PhotoSafeActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private Button againSafeSet;

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
