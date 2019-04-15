package com.mrchen.simpleh5andorid.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.mrchen.simpleh5andorid.App;
import com.mrchen.simpleh5andorid.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;




public class PackageHelper {
    private static final PackageManager pm;
    private static final int PERMISSION_GRANTED = 0;

    static {
        pm = App.getContext().getPackageManager();
    }

    /**
     * get versionName
     *
     * @return
     */
    public static String getVersionName() {
        try {
            return pm.getPackageInfo(getPackageName(), PERMISSION_GRANTED).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get versionCode
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            return pm.getPackageInfo(getPackageName(), PERMISSION_GRANTED).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * get packageName
     *
     * @return
     */
    public static String getPackageName() {
        return App.getContext().getPackageName();
    }

    /**
     * 获得应用名
     *
     * @return appPackage
     */
    public static String getAppName() {
        return App.getContext().getString(R.string.app_name);
    }

    /**
     * getMataDataString
     * <meta-data android:name="IS_CUSTOM" android:value="false" > </meta-data>
     *
     * @param mataDataName mataDataName==IS_CUSTOM
     * @return
     */
    public static String getMataDataString(String mataDataName) {
        ApplicationInfo appInfo = getApplicationInfo();
        if (null != appInfo) {
            return appInfo.metaData.getString(mataDataName);
        }
        return null;
    }

    /**
     * getMataDataBoolean
     * <meta-data android:name="IS_CUSTOM" android:value="false" > </meta-data>
     *
     * @param mataDataName mataDataName==IS_CUSTOM
     * @return
     */
    public static boolean getMataDataBoolean(String mataDataName) {
        ApplicationInfo appInfo = getApplicationInfo();
        if (null != appInfo) {
            return appInfo.metaData.getBoolean(mataDataName);
        }
        return false;
    }

    /**
     * get ApplicationInfo
     *
     * @return
     */
    private static ApplicationInfo getApplicationInfo() {
        try {
            return pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * skip AppStore
     *
     * @param paramContext
     */
    public static void openAppStore(Context paramContext) {
        paramContext.startActivity(new Intent(Intent.ACTION_VIEW, marketUri(getPackageName())));
    }

    private static Uri marketUri(String pkg) {
        return Uri.parse("market://details?id=" + pkg);
    }

    /**
     * 打开手机设置页面
     *
     * @param paramContext
     */
    public static void openSetting(Context paramContext) {
        if (null != paramContext) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            paramContext.startActivity(intent);
        }
    }


    /**
     * 判断指定包名的进程是否运行
     *
     * @param packageName
     * @return
     */
    public static boolean isRunning(String packageName) {
        ActivityManager am = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = am.getRunningTasks(100);
        boolean result = false;
        for (RunningTaskInfo info : runningTasks) {
            if (null != info && !TextUtils.isEmpty(info.baseActivity.getPackageName())
                    && info.baseActivity.getPackageName().equals(packageName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 本应用是否在前台运行
     *
     * @param paramContext
     * @return
     */
    public static boolean isAppRunning(Context paramContext) {
        ActivityManager am = (ActivityManager) paramContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        boolean result = false;
        for (RunningTaskInfo info : runningTasks) {
            if (null != info && !TextUtils.isEmpty(info.baseActivity.getPackageName())
                    && info.baseActivity.getPackageName().equals(getPackageName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 根据packageName 启动app
     *
     * @param paramContext
     * @param packageName
     */
    public static void openAppByPackage(Context paramContext, String packageName) {
        if (null != paramContext && null != packageName) {
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if (null != intent) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                paramContext.startActivity(intent);
            }
        }
    }

    /**
     * 检查某个应用是否安装
     *
     * @param packageName
     */
    public static boolean isAppInstall(String packageName) {
        try {
            return (null != pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 安装指定文件apk
     *
     * @param context
     * @param apkFile
     */
    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, (getPackageName() + ".fileprovider"), apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        }
    }

    /**
     * 判断是否自己签名，防止二次打包
     *
     * @param context
     * @param intent
     * @param checkSignature
     * @return
     */
    public static boolean canBeStarted(Context context, Intent intent, boolean checkSignature) {
        final PackageManager manager = context.getApplicationContext().getPackageManager();
        final ResolveInfo info = manager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            return false;
        }
        final ActivityInfo activityInfo = info.activityInfo;
        if (activityInfo == null) {
            return false;
        }
        if (!checkSignature) {
            return true;
        }

        return PackageManager.SIGNATURE_MATCH == manager.checkSignatures(context.getPackageName(), activityInfo.packageName);
    }

    /**
     * 通过签名对象的hashCode()方法获取一个Hash值，判断是否被重新签名
     *
     * @param paramContext
     * @param packageName
     * @return
     */
    public static int getSignature(Context paramContext, String packageName) {
        PackageManager pm = paramContext.getPackageManager();
        PackageInfo pi;
        int sig;
        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] s = pi.signatures;
            sig = s[0].hashCode();
        } catch (Exception e1) {
            sig = 0;
            e1.printStackTrace();
        }
        return sig;
    }

    /**
     * 签名保护，效验classes.dex文件hash值，来判断软件是否被重新打包,每次编译代码后crc都会改变
     *
     * @param paramContext
     * @return
     */
    public static boolean checkCRC(Context paramContext) {
        boolean beModified = false;
        long crc = 1107659220;
        ZipFile zf;
        try {
            zf = new ZipFile(paramContext.getPackageCodePath());
            ZipEntry ze = zf.getEntry("classes.dex");

            if (ze.getCrc() == crc) {
                beModified = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            beModified = false;
        }
        return beModified;
    }
}
