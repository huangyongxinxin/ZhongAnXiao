package com.dykj.zhonganxiao.util;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * @author 周竹
 * @file YoYoUtils
 * @brief
 * @date 2018/3/23 下午9:06
 * Copyright (c) 2017
 * All rights reserved.
 */

public class YoYoUtils {

    public static void shake(View view) {
        if (view != null)
            YoYo.with(Techniques.Shake).duration(700).playOn(view);

    }
}
