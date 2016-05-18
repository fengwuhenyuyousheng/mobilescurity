package com.test.yang.photosafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yang.photosafe.R;

/**这是设置Toast风格的自定义控件
 * Created by Administrator on 2016/5/17.
 */
public class SettingToastStyleItem extends LinearLayout{


    private TextView toastStyleDescription;
    private TextView toastStyleTitle;


    public SettingToastStyleItem(Context context) {
        super(context);
        initialize();
    }

    public SettingToastStyleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();

    }

    public SettingToastStyleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * 初始化自定义控件
     */
    public void initialize(){
        View view=View.inflate(getContext(),R.layout.item_set_toast_style,this);
        toastStyleDescription= (TextView) view.findViewById(R.id.toast_style_description);
        toastStyleTitle= (TextView) view.findViewById(R.id.toast_style_text);

    }

    /**
     * 设置选项的内容
     * @param toastStyleDescription 选项内容
     */
    public void setToastStyleDescription(String toastStyleDescription){
        this.toastStyleDescription.setText(toastStyleDescription);
    }

    /**
     * 设置选项标题
     * @param toastStyleTitle 选项标题
     */
    public void setToastStyleTitle(String toastStyleTitle){
        this.toastStyleTitle.setText(toastStyleTitle);
    }


}
