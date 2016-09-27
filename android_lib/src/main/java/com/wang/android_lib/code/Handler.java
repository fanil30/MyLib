package com.wang.android_lib.code;

import android.os.Message;

import java.lang.ref.WeakReference;

/**
 *  by Administrator on 2016/3/12.
 */
public class Handler {
    //    pub static MyHandler handler = new MyHandler(this);

    //    -------------------------------------Handler----------------------------------------------

    public static final int FINISH = 1;

    private static MainActivityHandler handler;

    public static void sendMessage(int what, Object obj) {
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.what = what;
            message.obj = obj;
            handler.sendMessage(message);
        }
    }


    public static class MainActivityHandler extends android.os.Handler {
        private WeakReference<MainActivity> weakReference;

        public MainActivityHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    activity.finish();
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    }

    class MainActivity {
        protected void finish() {

        }
    }
}
