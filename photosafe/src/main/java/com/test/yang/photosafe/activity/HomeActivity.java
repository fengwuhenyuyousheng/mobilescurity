package com.test.yang.photosafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    }
}
