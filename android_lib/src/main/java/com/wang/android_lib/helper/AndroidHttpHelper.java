package com.wang.android_lib.helper;

import android.content.Context;

import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.data_structure.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/8/18.
 */
public class AndroidHttpHelper {

    private Context context;
    private String dialogHint;
    private boolean showToastHint = true;
    private String requestMethod = "GET";
    private List<Pair<String, String>> requestPropertyList;
    private String output;
    private AndroidHttpUtil.OnSucceedListener succeedListener;
    private AndroidHttpUtil.OnFailedListener failedListener;


    public AndroidHttpHelper(Context context) {
        this.context = context;
        requestPropertyList = new ArrayList<>();
    }

    public void request(String url) {
        AndroidHttpUtil.startRequest(context, dialogHint, showToastHint, url, requestMethod,
                requestPropertyList, output, succeedListener, failedListener);
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
        requestPropertyList.add(new Pair<>("Set-Cookie", cookie));
        return this;
    }

    public AndroidHttpHelper addRequestProperty(String key, String value) {
        requestPropertyList.add(new Pair<>(key, value));
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
