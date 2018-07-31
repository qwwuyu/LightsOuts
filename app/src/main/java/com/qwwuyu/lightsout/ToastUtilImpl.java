package com.qwwuyu.lightsout;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by qiwei on 2016/12/13
 * copy过来 避免Toast队列
 */
public class ToastUtilImpl {
    /** 主线程Handler */
    private Handler handler = new Handler(Looper.getMainLooper());
    /** 主线程id */
    private long threadID = Looper.getMainLooper().getThread().getId();
    /** 懒加载单例Toast */
    private Toast toast;
    /** ApplicationContext */
    private Context appContext;
    private static ToastUtilImpl instance;

    static ToastUtilImpl getInstance() {
        if (instance == null) {
            synchronized (ToastUtilImpl.class) {
                if (instance == null) {
                    instance = new ToastUtilImpl();
                }
            }
        }
        return instance;
    }

    private ToastUtilImpl() {
    }

    public synchronized void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
        }
    }

    public void show(Object text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public void show(@StringRes int id) {
        show(id, Toast.LENGTH_SHORT);
    }

    public void show(Object text, int duration) {
        if (text == null) return;
        showToast(text.toString(), duration);
    }

    public void show(@StringRes int id, int duration) {
        showToast(appContext.getResources().getText(id), duration);
    }

    private void showToast(final CharSequence text, final int duration) {
        if (threadID == Thread.currentThread().getId()) {
            show(text, duration);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, duration);
                }
            });
        }
    }

    private void show(final CharSequence text, final int duration) {
        if (toast == null) {
            toast = makeText(appContext, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }

    /** 创建一个Toast */
    private Toast makeText(Context context, CharSequence text, int duration) {
        return Toast.makeText(context, text, duration);
    }
}