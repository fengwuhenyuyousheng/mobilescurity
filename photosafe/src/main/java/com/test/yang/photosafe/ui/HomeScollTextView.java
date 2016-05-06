package com.test.yang.photosafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**这是自定义滚动TextView
 * Created by Administrator on 2016/5/6.
 */
public class HomeScollTextView extends TextView{


    public HomeScollTextView(Context context) {
        super(context);
    }

    public HomeScollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeScollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean isFocused() {
        return true;
    }
}
