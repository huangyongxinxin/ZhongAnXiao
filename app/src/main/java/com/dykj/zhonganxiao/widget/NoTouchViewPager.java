package com.dykj.zhonganxiao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @file: NoTouchViewPager 不能滑动
 * @author: guokang
 * @date: 2019-07-16
 */
public class NoTouchViewPager extends ViewPager {

    public NoTouchViewPager(Context context) {
        super(context);
    }

    public NoTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    ②、返回值为False，代表不拦截这次事件，不进入到ViewGroup的onTouchEvent中，直接进入到View的onTouchEvent中
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
