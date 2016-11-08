package com.wang.android_lib.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 参考1：http://www.aichengxu.com/view/48636  android 用户头像，图片裁剪，上传并附带用户数据base64code
 * 参考2：http://www.cnblogs.com/w-y-f/p/4028379.html  Android 拍照或从相册取图片并裁剪 - ※WYF※ - 博客园
 * http://www.cnblogs.com/zhengbeibei/archive/2013/04/01/2994215.html  Intent 的各种打开文件
 */
public class IntentUtil {

    public static Intent getCallIntent(String phoneNumber) {
        return new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
    }

    /**
     * 图片选定后的获取方式：onActivityResult->if(data!=null) Uri uri = data.getData(); uri.getPath;
     *
     * @return 通过相册得到图片的意图
     */
    public static Intent getImageFromAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * @return 通过拍摄得到图片的意图
     */
    public static Intent getImageFromCameraIntent(Uri saveUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        return intent;
    }

    /**
     * @param picUri  需要进行裁剪的图片的Uri。获取方式：1.intent.getData() 2.Uri.fromFile(file)
     * @param saveUri 指定裁剪后保存的Uri。获取方式：Uri.fromFile(file)
     * @param square  是否限制裁剪为正方形
     * @return 返回请求裁剪的意图
     */
    public static Intent getPhotoZoneIntent(Uri picUri, Uri saveUri, boolean square) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(picUri, "image/*");
        intent.putExtra("crop", "true");//设置显示的View可裁剪
        if (square) {
            intent.putExtra("aspectX", 1);//宽比例
            intent.putExtra("aspectY", 1);//高比例
//            intent.putExtra("outputX", 1000);//实际宽
//            intent.putExtra("outputY", 1000);//实际高
        }
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true); // no face detectio
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        return intent;
    }

    public static Intent getUrlIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    public static Intent getImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }

    /**
     * Intent 打开文件浏览器 http://blog.csdn.net/waww116529/article/details/51426873
     */
    public static Intent getFileIntent() {
        // 打开系统文件浏览功能  
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    /**
     * http://blog.csdn.net/sp6645597/article/details/20215563
     * android Uri获取真实路径转换成File的方法
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
