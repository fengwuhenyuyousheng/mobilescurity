package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.engine.SoftWareInfoEngine;
import com.test.yang.photosafe.model.AppInfo;
import com.test.yang.photosafe.tools.MyAsycnTaks;

import java.util.ArrayList;
import java.util.List;

/**这是软件管理界面
 * Created by Administrator on 2016/5/20.
 */
public class SoftwareManagementActivity extends AppCompatActivity implements View.OnClickListener{

    private List<AppInfo> appInfoList;
    private ListView softwareListView;
    private ProgressBar loadProgressBar;
    private MySoftwareInfoAdapter mySoftwareInfoAdapter;
    private List<AppInfo> userSoftwareList;
    private List<AppInfo> systemSoftwareList;
    private TextView softwareCountTextView;
    private PopupWindow popupWindow;
    private AppInfo appInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_roll);
        userSoftwareList=new ArrayList<AppInfo>();
        systemSoftwareList=new ArrayList<AppInfo>();
        softwareListView= (ListView) findViewById(R.id.software_list_view);
        loadProgressBar= (ProgressBar) findViewById(R.id.load_progress_bar);
        softwareCountTextView= (TextView) findViewById(R.id.software_count);
        appInfo=new AppInfo();
        loadSoftwareInfo();
        listViewOnScroll();
        listViewItemClick();

    }

    /**
     * listview滑动监听事件
     */
    private void listViewOnScroll() {
        softwareListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滑动状态改变的时候调用
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }
            //滑动的时候调用
            //view : listview
            //firstVisibleItem : 界面第一个显示条目
            //visibleItemCount : 显示条目总个数
            //totalItemCount : 条目的总个数
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //为null的原因:listview在初始化的时候就会调用onScroll方法
                if(firstVisibleItem==0){
                    softwareCountTextView.setVisibility(View.GONE);
                }else{
                    softwareCountTextView.setVisibility(View.VISIBLE);
                }
                if (userSoftwareList != null && systemSoftwareList != null) {
                    if (firstVisibleItem >= userSoftwareList.size()+1) {
                        softwareCountTextView.setText("系统程序("+systemSoftwareList.size()+")");
                    }else{
                        softwareCountTextView.setText("用户程序("+userSoftwareList.size()+")");
                    }
                }
            }
        });
    }

    /**
     * 异步加载所有程序信息
     */
    private void loadSoftwareInfo(){
        new MyAsycnTaks(){

            @Override
            public void preTask() {
                loadProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {
                appInfoList= SoftWareInfoEngine.getAppInfo(getApplicationContext());
                for(AppInfo appinfo:appInfoList){
                    if(appinfo.getIsUser()){
                        userSoftwareList.add(appinfo);
                    }else{
                        systemSoftwareList.add(appinfo);
                    }
                }

            }

            @Override
            public void posTask() {
                if (mySoftwareInfoAdapter == null){
                    mySoftwareInfoAdapter=new MySoftwareInfoAdapter();
                    softwareListView.setAdapter(mySoftwareInfoAdapter);
                }else{
                    mySoftwareInfoAdapter.notifyDataSetChanged();
                }
                loadProgressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    /**
     * 条目点击事件
     */
    private void listViewItemClick() {
        softwareListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //view : 条目的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //弹出气泡
                //1.屏蔽用户程序和系统程序(...个)弹出气泡
                if (position == 0 || position == userSoftwareList.size()+1) {
                    return;
                }
                //2.获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if (position <= userSoftwareList.size()) {
                    //用户程序
                    appInfo = userSoftwareList.get(position-1);
                }else{
                    //系统程序
                    appInfo = systemSoftwareList.get(position - userSoftwareList.size() - 2);
                }
                //5.弹出新的气泡之前,删除旧 的气泡
                hidePopupWindow();
                //3.弹出气泡
				/*TextView contentView = new TextView(getApplicationContext());
				contentView.setText("我是popuwindow的textview控件");
				contentView.setBackgroundColor(Color.RED);*/
                View contentView = View.inflate(getApplicationContext(), R.layout.popup_window, null);
                LinearLayout popupWindowUninstall= (LinearLayout) contentView.findViewById(R.id.popup_window_uninstall);
                LinearLayout popupWindowStart= (LinearLayout) contentView.findViewById(R.id.popup_window_start);
                LinearLayout popupWindowShare= (LinearLayout) contentView.findViewById(R.id.popup_window_share);
                LinearLayout popupWindowDetail= (LinearLayout) contentView.findViewById(R.id.popup_window_detail);

                popupWindowUninstall.setOnClickListener(SoftwareManagementActivity.this);
                popupWindowStart.setOnClickListener(SoftwareManagementActivity.this);
                popupWindowShare.setOnClickListener(SoftwareManagementActivity.this);
                popupWindowDetail.setOnClickListener(SoftwareManagementActivity.this);


                //contentView : 显示view对象
                //width,height : view宽高
                popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //4.获取条目的位置,让气泡显示在相应的条目
                int[] location = new int[2];//保存x和y坐标的数组
                view.getLocationInWindow(location);//获取条目x和y的坐标,同时保存到int[]
                //获取x和y的坐标
                int x = location[0];
                int y = location[1];
                //parent : 要挂载在那个控件上
                //gravity,x,y : 控制popuwindow显示的位置
                popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x+100, y);
                //添加背景

                //缩放动画
                //前四个 :　控制控件由没有变到有   动画 0:没有    1:整个控件
                //后四个:控制控件是按照自身还是父控件进行变化
                //RELATIVE_TO_SELF : 以自身变化
                //RELATIVE_TO_PARENT : 以父控件变化
                ScaleAnimation scaleAnimation=new ScaleAnimation(0,1.0f,0,1.0f, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setDuration(1000);

                //透明度动画
                AlphaAnimation alphaAnimation=new AlphaAnimation(0.3f,1.0f);
                alphaAnimation.setDuration(2000);

                //动画插值器
                AnimationSet animationSet=new AnimationSet(true);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);

                //使用动画
                contentView.startAnimation(animationSet);

            }

        });
    }

    private void hidePopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();//隐藏气泡
            popupWindow = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popup_window_uninstall:
                uninstallSoftware();
                break;
            case R.id.popup_window_start:
                start();
                break;
            case R.id.popup_window_share:
                share();
                break;
            case R.id.popup_window_detail:
                detail();
                break;
            default:
                break;
        }
    }

    /**
     * 定义分享事件
     */
    private void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "发现一个很牛x软件"+appInfo.getAppName()+",下载地址:www.baidu.com,自己去搜");
        startActivity(shareIntent);
    }

    /**
     * 定义详情事件
     */
    private void detail() {
        Intent detailIntent=new Intent();
        detailIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        detailIntent.setData(Uri.parse("package:"+appInfo.getPackageName()));
        startActivity(detailIntent);
    }

    /**
     * 定义卸载事件
     */
    private void uninstallSoftware(){
        if(appInfo.getIsUser()){
            if(!appInfo.getPackageName().equals(getPackageName())){
                Intent uninstallIntent=new Intent();
                uninstallIntent.setAction("android.intent.action.DELETE");
                uninstallIntent.addCategory("android.intent.category.DEFAULT");
                uninstallIntent.setData(Uri.parse("package:"+appInfo.getPackageName()));
                startActivityForResult(uninstallIntent,0);
            }else{
                Toast.makeText(getApplicationContext(), "无法卸载自身程序",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "卸载系统程序需要root权限",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 定义启动事件
     */
    private void start(){
        PackageManager packageManager=getPackageManager();
        Intent startIntent=packageManager.getLaunchIntentForPackage(appInfo.getPackageName());
        if(startIntent!=null){
            startActivity(startIntent);
        }else{
            Toast.makeText(getApplicationContext(), "系统核心程序，无法启动",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadSoftwareInfo();
    }

    private class MySoftwareInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return userSoftwareList.size()+systemSoftwareList.size()+2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                //添加用户程序(...个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("用户程序("+userSoftwareList.size()+")");
                return textView;
            }else if(position == userSoftwareList.size()+1){
                //添加系统程序(....个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统程序("+systemSoftwareList.size()+")");
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout){
                view=convertView;
                viewHolder= (ViewHolder) view.getTag();
            }else{
                view=View.inflate(getApplicationContext(),R.layout.item_app_info,null);
                viewHolder=new ViewHolder();
                viewHolder.softwareNameText= (TextView) view.findViewById(R.id.software_name_text);
                viewHolder.softwareInstallation= (TextView) view.findViewById(R.id.software_installation);
                viewHolder.softwareVersion= (TextView) view.findViewById(R.id.software_version);
                viewHolder.softwareIconImage= (ImageView) view.findViewById(R.id.software_icon_image);
                view.setTag(viewHolder);
            }
            //1.获取应用程序的信息
            AppInfo appInfo = null;
            //数据就要从userappinfo和systemappinfo中获取
            if (position <= userSoftwareList.size()) {
                //用户程序
                appInfo = userSoftwareList.get(position-1);
            }else{
                //系统程序
                appInfo = systemSoftwareList.get(position - userSoftwareList.size() - 2);
            }

//            Log.d("SoftwareManagement",appInfo.toString());
            viewHolder.softwareNameText.setText(appInfo.getAppName());
            if (appInfo.getIsSDCard()){
                viewHolder.softwareInstallation.setText("安装位置: SD卡");
            }else{
                viewHolder.softwareInstallation.setText("安装位置: 手机内存");
            }
            viewHolder.softwareVersion.setText("版本: "+appInfo.getVersionName());
            viewHolder.softwareIconImage.setImageDrawable(appInfo.getAppIcon());
            return view;
        }
    }

    private static class ViewHolder{
        TextView softwareNameText,softwareInstallation,softwareVersion;
        ImageView softwareIconImage;
    }
}