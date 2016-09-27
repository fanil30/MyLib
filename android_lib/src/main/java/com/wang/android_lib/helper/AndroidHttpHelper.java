package com.wang.android_lib.helper;

import android.content.Context;

import com.wang.android_lib.util.AndroidHttpUtil;

/**
 * by 王荣俊 on 2016/8/18.
 */
public class AndroidHttpHelper {

    private Context context;
    private String dialogHint;
    private boolean showToastHint = true;
    private String requestMethod = "GET";
    private String cookie;
    private String output;
    private AndroidHttpUtil.OnSucceedListener succeedListener;
    private AndroidHttpUtil.OnFailedListener failedListener;

    public AndroidHttpHelper(Context context) {
        this.context = context;
    }

    public void request(String url) {
        AndroidHttpUtil.startRequest(context, dialogHint, showToastHint, url, requestMethod,
                cookie, output, succeedListener, failedListener);
        requestMethod = "GET";
        dialogHint = "提示";
        showToastHint = true;
    }

    public AndroidHttpHelper setDialogHint(String dialogHint) {
        this.dialogHint = dialogHint;
        return this;
    }

    public AndroidHttpHelper setShowToastHint(boolean showToastHint) {
        this.showToastHint = showToastHint;
        return this;
    }

    public AndroidHttpHelper setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public AndroidHttpHelper setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public AndroidHttpHelper setOutput(String output) {
        this.output = output;
        return this;
    }

    public AndroidHttpHelper setOnSucceedListener(AndroidHttpUtil.OnSucceedListener succeedListener) {
        this.succeedListener = succeedListener;
        return this;
    }

    public AndroidHttpHelper setOnFailedListener(AndroidHttpUtil.OnFailedListener failedListener) {
        this.failedListener = failedListener;
        return this;
    }

}
