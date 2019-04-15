package com.mrchen.simpleh5andorid;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.mrchen.simpleh5andorid.handler.UnceHandler;
import com.mrchen.simpleh5andorid.utils.PackageHelper;

import java.io.File;
import java.util.concurrent.TimeUnit;



/**
 * @Author: Chens
 * @E-mail:
 * @Date: 2017-12-27
 * @Time: 14:20
 * @DateModified: 2018-01-09
 * @Hint:
 * @Param <T>
 * FIXME
 */
public class App extends MultiDexApplication {

    private static Context mContext;

    private static boolean isDebug;



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initCatchException();
        isDebug = PackageHelper.getMataDataBoolean(getString(R.string.is_debug));
    }


    public static boolean isDebug() {
        return isDebug;
    }



    public static Context getContext() {
        return mContext;
    }

    public static Resources getAppResource() {
        return mContext.getResources();
    }

    public static int getResColor(int id) {
        return mContext.getResources().getColor(id);
    }

    public static String getResStr(int resId) {
        return mContext.getString(resId);
    }

    public static String getResStr(int resId, Object... formatArgs) {
        return mContext.getString(resId, formatArgs);
    }

    //捕获全局Exception 重启界面
    public void initCatchException() {
        //设置该CrashHandler为程序的默认处理器
        UnceHandler catchExcep = new UnceHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }
}