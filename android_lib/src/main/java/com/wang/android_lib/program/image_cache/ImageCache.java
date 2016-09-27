package com.wang.android_lib.program.image_cache;

import android.view.View;

import com.wang.java_util.TextUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * by 王荣俊 on 2016/9/22.
 * http://www.cnblogs.com/dolphin0520/p/3932921.html Java并发编程：线程池的使用
 */
public class ImageCache {

    private static final int CONNECT_TIME_OUT = 5000;
    private static final int READ_TIME_OUT = 8000;

    private static ImageCache imageCache;

    private int imageAliveTime = Integer.MAX_VALUE;//缓存图片生存期，默认无限期
    private ThreadPoolExecutor pool;
    private OnCallbackListener listener;
    private ImageCacheFileNameRule rule = new ImageCacheFileNameRule() {
        @Override
        public String toFileName(String imageUrl) {
            return TextUtil.correctFileName(imageUrl, "_") + ".jpg";
        }
    };

    public int getImageAliveTime() {
        return imageAliveTime;
    }

    public void setImageAliveTime(int imageAliveTime) {
        this.imageAliveTime = imageAliveTime;
    }

    public ImageCacheFileNameRule getImageCacheFileNameRule() {
        return rule;
    }

    public void setImageCacheFileNameRule(ImageCacheFileNameRule rule) {
        this.rule = rule;
    }

    private ImageCache() {

    }

    public static ImageCache getImageCache() {
        if (imageCache == null) {
            imageCache = new ImageCache();
        }
        if (imageCache.pool == null) {
            //TODO 需要经过多种参数组合才能确定性能的最优
            imageCache.pool = new ThreadPoolExecutor(5, 20, 20, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
        return imageCache;
    }

    public void get(View view, String imageUrl, String cacheDir) {
        ImageThread thread = new ImageThread(view, imageUrl, cacheDir, listener, rule, imageAliveTime);
        pool.execute(thread);
    }

    public void setOnCallbackListener(OnCallbackListener listener) {
        this.listener = listener;
    }

    public interface OnCallbackListener {
        void onPreGet(String imageUrl, View view);

        void onGetNotInCache(String imageUrl, View view);

        void onGetSuccess(String imageUrl, String imagePath, View view, boolean isInCache);

        void onGetFailed(String imageUrl, String imagePath, View view, Exception e);
    }

    public interface ImageCacheFileNameRule {
        String toFileName(String imageUrl);
    }

    static class ImageThread implements Runnable {

        private View view;
        private String imageUrl;
        private String cacheDir;
        private OnCallbackListener listener;
        private ImageCacheFileNameRule rule;
        private int imageAliveTime;

        public ImageThread(View view, String imageUrl, String cacheDir, OnCallbackListener listener,
                           ImageCacheFileNameRule rule, int imageAliveTime) {
            this.view = view;
            this.imageUrl = imageUrl;
            this.cacheDir = cacheDir;
            this.listener = listener;
            this.rule = rule;
            this.imageAliveTime = imageAliveTime;
        }

        @Override
        public void run() {

            listener.onPreGet(imageUrl, view);

            String imagePath = cacheDir + File.separator + rule.toFileName(imageUrl);
            File imageFile = new File(imagePath);
            boolean alive = System.currentTimeMillis() - imageFile.lastModified() <= imageAliveTime;
            if (imageFile.exists() && alive) {
                listener.onGetSuccess(imageUrl, imagePath, view, true);
                return;
            } else if (imageFile.exists()) {
                imageFile.delete();
            }
            listener.onGetNotInCache(imageUrl, view);

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(imageUrl).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(CONNECT_TIME_OUT);
                conn.setReadTimeout(READ_TIME_OUT);

                FileOutputStream fos = new FileOutputStream(imagePath);
                InputStream is = conn.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();

                listener.onGetSuccess(imageUrl, imagePath, view, false);

            } catch (Exception e) {
                listener.onGetFailed(imageUrl, imagePath, view, e);
            }

        }

    }

}
