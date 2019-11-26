package com.dykj.zhonganxiao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @file: CustomViewPager
 * @author: guokang
 * @date: 2019-09-02
 */
public class CustomViewPager extends ViewPager {
    private HashMap<Integer, View> mMap = new LinkedHashMap<>();

    private int current;
    private int height = 0;

    private boolean scrollable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mMap.size() > current) {
            View child = getChildAt(current);
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
        }

//        得到ViewPager的MeasureSpec，使用固定值和MeasureSpec.EXACTLY，
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    // 重置页面高度
    public void resetHeight(int current) {
        this.current = current;
        if (mMap.size() > current) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            } else {
                layoutParams.height = height;
            }
            setLayoutParams(layoutParams);
        }
    }

    public void setObjectForPosition(View view, int position) {
        mMap.put(position, view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
