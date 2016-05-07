package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.adapters.HomeGridViewAdapter;

/**这是主界面
 * Created by Administrator on 2016/5/4.
 */
public class HomeActivity extends AppCompatActivity {

    private GridView mHomeGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHomeGridView= (GridView) findViewById(R.id.home_grid_view);
        mHomeGridView.setAdapter(new HomeGridViewAdapter(getApplicationContext()));
        mHomeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        Intent jumpSetingActivity=new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(jumpSetingActivity);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
