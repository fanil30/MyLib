package com.wang.android_lib.manager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.wang.android_lib.util.IntentUtil;
import com.wang.android_lib.util.M;
import com.wang.java_util.FileUtil;

import java.io.File;

/**
 * 图片选择与上传的管理类，必须的步骤：在onActivityResult中调用handleActivityResult
 */
public class SelectImageManager {

    public static final int ALBUM = 1;
    public static final int CAMERA = 2;
    public static final int ZONE = 3;

    private Activity activity;
    private String capturePath;
    private String zonePath;
    private boolean square;

    public interface OnSelectImageFinishListener {
        void onSelectImageFinish(String imagePath);
    }

    private OnSelectImageFinishListener listener;

    public void setOnFinishListener(OnSelectImageFinishListener listener) {
        this.listener = listener;
    }

    public SelectImageManager(Activity activity, String capturePath, String zonePath) {
        this.activity = activity;
        this.capturePath = capturePath;
        this.zonePath = zonePath;
        cleanOldImage();
    }

    public void selectFromAlbum(boolean square) {
        this.square = square;

        //不管上次是否上传成功，先删除上次选择后遗留下来的图片
        cleanOldImage();

        Intent intent = IntentUtil.getImageFromAlbumIntent();
        activity.startActivityForResult(intent, ALBUM);
    }

    public void selectFromCamera(boolean square) {
        this.square = square;

        //不管上次是否上传成功，先删除上次选择后遗留下来的图片
        cleanOldImage();

        Intent intent = IntentUtil.getImageFromCameraIntent(getUri(capturePath));
        activity.startActivityForResult(intent, CAMERA);
    }

    public void handleActivityResult(int requestCode, Intent data) {
        switch (requestCode) {

            case CAMERA://拍照完成
                FileUtil.delete(zonePath);//先删除上次修改头像遗留下来的图片
                Intent intent = IntentUtil.getPhotoZoneIntent(getUri(capturePath),
                        getUri(zonePath), square);
                activity.startActivityForResult(intent, ZONE);
                break;

            case ALBUM://相册选取完成
                if (data != null) {
                    Uri uri = data.getData();
                    intent = IntentUtil.getPhotoZoneIntent(uri, getUri(zonePath), square);
                    activity.startActivityForResult(intent, ZONE);
                } else {
                    M.t(activity, "已取消");
                }
                break;

            case ZONE://裁剪完成
                if (listener != null && new File(zonePath).exists()) {
                    listener.onSelectImageFinish(zonePath);
                }
                break;

        }

    }

    private Uri getUri(String imagePath) {
        return Uri.fromFile(new File(imagePath));
    }

    private void cleanOldImage() {
        FileUtil.delete(capturePath);
        FileUtil.delete(zonePath);
    }

}
