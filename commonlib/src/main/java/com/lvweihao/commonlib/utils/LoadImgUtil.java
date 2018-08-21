package com.lvweihao.commonlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lvweihao.commonlib.R;
import com.lvweihao.commonlib.view.GlideCircleTransform;

/**
 * 图片加载
 * Created by Administrator on 2017/8/23 0023.
 */

public class LoadImgUtil {

    public static void loadImage(Context context, String url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().into(img);
    }

    public static void loadImage(Context context, int url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().into(img);
    }

    public static void loadImage(Context context, Uri url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().into(img);
    }

    /**
     * 加载圆形头像
     *
     * @param context
     * @param url
     * @param img
     */
    public static void loadCircleImage(Context context, Uri url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().placeholder(R.mipmap.default_avatar)
                .transform(new GlideCircleTransform(context))
                .into(img);
    }

    public static void loadCircleImageByUrl(Context context, String url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().placeholder(R.mipmap.default_avatar)
                .transform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(img);
    }
}
