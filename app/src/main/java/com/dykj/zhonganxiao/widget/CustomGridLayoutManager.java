package com.dykj.zhonganxiao.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @file: CustomGridLayoutManager
 * @author: guokang
 * @date: 2019-09-04
 */
public class CustomGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;
    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {

        return isScrollEnabled && super.canScrollVertically();
    }
}

