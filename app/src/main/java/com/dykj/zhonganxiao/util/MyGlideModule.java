package com.dykj.zhonganxiao.util;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @file: MyGlideModule
 * @author: guokang
 * @date: 2019-09-23
 */
@GlideModule
public final class MyGlideModule extends AppGlideModule {
    /** * MemorySizeCalculator类通过考虑设备给定的可用内存和屏幕大小想出合理的默认大小. * 通过LruResourceCache进行缓存。 * @param context * @param builder */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        /*MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context) .setMemoryCacheScreens(2) .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));*/
        int diskCacheSizeBytes = 1024 * 1024 * 500; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));



    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }



}