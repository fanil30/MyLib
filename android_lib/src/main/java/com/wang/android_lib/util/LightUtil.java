package com.wang.android_lib.util;

import android.hardware.Camera;

/**
 *  by 王荣俊 on 2016/9/12.
 */
public class LightUtil {

    public static Camera camera;

    public static void open() {

        if (camera == null) {
            camera = Camera.open();
        }
        camera.startPreview();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启  
        camera.setParameters(parameters);
    }

    public static void close() {

        if (camera == null) {
            camera = Camera.open();
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭  
        camera.setParameters(parameters);
    }
}
