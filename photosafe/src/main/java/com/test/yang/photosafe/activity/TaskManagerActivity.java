package com.test.yang.photosafe.activity;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.model.TaskInfo;
import com.test.yang.photosafe.tools.MyAsycnTaks;
import com.test.yang.photosafe.tools.TaskTools;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {

    private ListView taskListView;
    private ProgressBar loadProgressBar;
    private Button selectAllButton;
    private Button cancelAllButton;
    private Button clearSelectButton;
    private Button clearSettingButton;
    private TextView runningProgressCount;
    private TextView ramAvailableText;
    private List<TaskInfo> taskInfoList;
    private List<TaskInfo> userTaskInfoList;
    private List<TaskInfo> systemTaskInfoList;
    private TaskInfo taskInfo;
    private MyTaskInfoAdapter myTaskInfoAdapter;
    private int processCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        taskListView= (ListView) findViewById(R.id.task_list_view);
        loadProgressBar= (ProgressBar) findViewById(R.id.load_progress_bar);
        selectAllButton= (Button) findViewById(R.id.select_all_button);
        cancelAllButton= (Button) findViewById(R.id.cancel_all_button);
        clearSelectButton= (Button) findViewById(R.id.clear_select_button);
        clearSettingButton= (Button) findViewById(R.id.clear_setting_button);
        runningProgressCount= (TextView) findViewById(R.id.running_progress_count);
        ramAvailableText= (TextView) findViewById(R.id.ram_available_text);
        userTaskInfoList=new ArrayList<TaskInfo>();
        systemTaskInfoList=new ArrayList<TaskInfo>();
        loadRunningProcessInfo();
        showProgressCountAndRAM();
        listViewItemClick();
    }

    /**
     * 定义显示进程数和可用内存
     */
    private void showProgressCountAndRAM(){
        // 设置显示数据
        // 获取相应的数据
        // 获取运行的进程个数
        processCount = TaskTools.getProcessCount(getApplicationContext());
        runningProgressCount.setText("运行中进程:" + processCount + "个");
        // 获取剩余,总内存'
        long availableRam = TaskTools.getAvailableRam(getApplicationContext());
        // 数据转化
        String availableRAM = Formatter.formatFileSize(getApplicationContext(),
                availableRam);
        // 获取总内存
        // 根据不同的sdk版去调用不同的方法
        // 1.获取当前的sdk版本
        long totalRam= TaskTools.getTotalRam();
        // 数据转化
        String totalRAM = Formatter.formatFileSize(getApplicationContext(),
                totalRam);
        ramAvailableText.setText("剩余/总内存:" + availableRAM + "/"
                + totalRAM);
    }


    /**
     * 定义选择操作
     * @param v
     */
    public void selectAll(View v){
        //用户进程
        for (int i = 0; i < userTaskInfoList.size(); i++) {
            if (!userTaskInfoList.get(i).getPackageName().equals(getPackageName())) {
                userTaskInfoList.get(i).setChecked(true);
            }
        }
        //系统进程
        for (int i = 0; i < systemTaskInfoList.size(); i++) {
            systemTaskInfoList.get(i).setChecked(true);
        }
        //更新界面
        myTaskInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 定义取消操作
     * @param v
     */
    public void cancelAll(View v){
        //用户进程
        for (int i = 0; i < userTaskInfoList.size(); i++) {
            userTaskInfoList.get(i).setChecked(false);
        }
        //系统进程
        for (int i = 0; i < systemTaskInfoList.size(); i++) {
            systemTaskInfoList.get(i).setChecked(false);
        }
        //更新界面
        myTaskInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 定义清理操作
     * @param v
     */
    public void clearSelect(View v){
        //1.获取进程的管理者
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //保存杀死进程信息的集合
        List<TaskInfo> deleteTaskInfoList = new ArrayList<TaskInfo>();

        for (int i = 0; i < userTaskInfoList.size(); i++) {
            if (userTaskInfoList.get(i).isChecked()) {
                //杀死进程
                //packageName : 进程的包名
                //杀死后台进程
                activityManager.killBackgroundProcesses(userTaskInfoList.get(i).getPackageName());
                deleteTaskInfoList.add(userTaskInfoList.get(i));//将杀死的进程信息保存的集合中
            }
        }
        //系统进程
        for (int i = 0; i < systemTaskInfoList.size(); i++) {
            if (systemTaskInfoList.get(i).isChecked()) {
                //杀死进程
                //packageName : 进程的包名
                //杀死后台进程
                activityManager.killBackgroundProcesses(systemTaskInfoList.get(i).getPackageName());
                deleteTaskInfoList.add(systemTaskInfoList.get(i));//将杀死的进程信息保存的集合中
            }
        }
        long memory=0;
        //遍历deleteTaskInfoList,分别从 userTaskInfoList和systemTaskInfoList中删除deleteTaskInfoList中的数据
        for (TaskInfo deleteTaskInfo: deleteTaskInfoList) {
            if (deleteTaskInfo.isUser()) {
                userTaskInfoList.remove(deleteTaskInfo);
            }else{
                systemTaskInfoList.remove(deleteTaskInfo);
            }
            memory+=deleteTaskInfo.getRamSize();
        }

        //数据转化
        String deleteSize = Formatter.formatFileSize(getApplicationContext(), memory);
        Toast.makeText(getApplicationContext(), "共清理"+deleteTaskInfoList.size()+"个进程,释放"+deleteSize+"内存空间", Toast.LENGTH_SHORT).show();

        // 更改运行中的进程个数以及剩余总内存
        processCount = processCount - deleteTaskInfoList.size();
        runningProgressCount.setText("运行中进程:" + processCount + "个");

        // 更改剩余总内存,重新获取剩余总内存
        // 获取剩余,总内存'
        long availableRam = TaskTools.getAvailableRam(getApplicationContext());
        // 数据转化
        String availableRAM = Formatter.formatFileSize(getApplicationContext(),
                availableRam);

        long totalRam=TaskTools.getTotalRam();
        // 数据转化
        String totalRAM = Formatter.formatFileSize(getApplicationContext(),
                totalRam);
        ramAvailableText.setText("剩余/总内存:" + availableRAM + "/"
                + totalRAM);

        //为下次清理进程做准备
        deleteTaskInfoList.clear();
        deleteTaskInfoList=null;
        //更新界面
        myTaskInfoAdapter.notifyDataSetChanged();

    }

    /**
     * 定义设置操作
     * @param v
     */
    public void clearSetting(View v){

    }

    /**
     * 异步加载所有进程信息
     */
    private void loadRunningProcessInfo(){
        new MyAsycnTaks(){

            @Override
            public void preTask() {
                loadProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {
                taskInfoList= TaskTools.getAllTaskInfo(getApplicationContext());
                for(TaskInfo taskInfo:taskInfoList){
                    if(taskInfo.isUser()){
                        userTaskInfoList.add(taskInfo);
                    }else{
                        systemTaskInfoList.add(taskInfo);
                    }
                }

            }

            @Override
            public void posTask() {
                if (myTaskInfoAdapter == null){
                    myTaskInfoAdapter=new MyTaskInfoAdapter();
                    taskListView.setAdapter(myTaskInfoAdapter);
                }else{
                    myTaskInfoAdapter.notifyDataSetChanged();
                }
                loadProgressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private class MyTaskInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userTaskInfoList.size()+systemTaskInfoList.size()+2;
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
                textView.setText("用户程序("+userTaskInfoList.size()+")");
                return textView;
            }else if(position == userTaskInfoList.size()+1){
                //添加系统程序(....个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统程序("+systemTaskInfoList.size()+")");
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout){
                view=convertView;
                viewHolder= (ViewHolder) view.getTag();
            }else{
                view=View.inflate(getApplicationContext(),R.layout.item_task_info,null);
                viewHolder=new ViewHolder();
                viewHolder.taskNameText= (TextView) view.findViewById(R.id.task_name_text);
                viewHolder.taskRamSpaceText= (TextView) view.findViewById(R.id.task_ram_space_text);
                viewHolder.selectTaskCheckBox= (CheckBox) view.findViewById(R.id.select_task_checkBox);
                viewHolder.taskIconImage= (ImageView) view.findViewById(R.id.task_icon_image);
                view.setTag(viewHolder);
            }
            //1.获取应用程序的信息
            TaskInfo taskInfo = null;
            //数据就要从userTaskInfoList和systemTaskInfoList中获取
            if (position <= userTaskInfoList.size()) {
                //用户程序
                taskInfo = userTaskInfoList.get(position-1);
            }else{
                //系统程序
                taskInfo = systemTaskInfoList.get(position - userTaskInfoList.size() - 2);
            }

//            Log.d("SoftwareManagement",appInfo.toString());
            if(TextUtils.isEmpty(taskInfo.getName())){
                viewHolder.taskNameText.setText(taskInfo.getPackageName());
            }else {
                viewHolder.taskNameText.setText(taskInfo.getName());
            }
            //数据转化
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(), taskInfo.getRamSize());
            viewHolder.taskRamSpaceText.setText("占用内存: "+formatFileSize);
            if (taskInfo.getIcon() == null) {
                viewHolder.taskIconImage.setImageResource(R.mipmap.ic_launcher);
            }else{
                viewHolder.taskIconImage.setImageDrawable(taskInfo.getIcon());
            }
            if(taskInfo.isChecked()){
                viewHolder.selectTaskCheckBox.setChecked(true);
            }else{
                viewHolder.selectTaskCheckBox.setChecked(false);
            }
            //判断如果是我们的应用程序,就把checkbox隐藏,不是的话显示,在getview中有if必须有else
            if (taskInfo.getPackageName().equals(getPackageName())) {
                viewHolder.selectTaskCheckBox.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.selectTaskCheckBox.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }

    private static class ViewHolder{
        TextView taskNameText,taskRamSpaceText;
        ImageView taskIconImage;
        CheckBox selectTaskCheckBox;
    }

    /**
     * listView条目点击事件
     */
    private void listViewItemClick() {
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view : 条目的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //动态改变checkbox状态
                if (position == 0 || position == userTaskInfoList.size()+1) {
                    return;
                }
                //2.获取条目所对应的应用程序的信息
                //数据就要从userTaskInfoList和systemTaskInfoList中获取
                if (position <= userTaskInfoList.size()) {
                    //用户程序
                    taskInfo = userTaskInfoList.get(position-1);
                }else{
                    //系统程序
                    taskInfo = systemTaskInfoList.get(position - userTaskInfoList.size() - 2);
                }
                //3.根据之前保存的checkbox的状态设置点击之后的状态,原先选中,点击之后不选中
                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                }else{
                    //如果是当前应用不能设置成true
                    if (!taskInfo.getPackageName().equals(getPackageName())) {
                        taskInfo.setChecked(true);
                    }
                }
                //只更新点击的条目
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.selectTaskCheckBox.setChecked(taskInfo.isChecked());
            }
        });
    }
}
