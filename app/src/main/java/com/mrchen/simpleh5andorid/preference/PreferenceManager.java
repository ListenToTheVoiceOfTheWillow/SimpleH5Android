package com.mrchen.simpleh5andorid.preference;

import android.content.Context;

import com.mrchen.simpleh5andorid.App;
import com.mrchen.simpleh5andorid.preference.PreferencesUtils.ParamType;



public class PreferenceManager {
    private static PreferencesUtils preferencesUtils;// 配置项管理对象
    private static final String ConfigName = "index_";

    static {
        preferencesUtils = PreferencesUtils.getPreferences(App.getContext());
    }

    public static PreferencesUtils getPreferencesUtils(Context paramContext) {
        if (null == preferencesUtils) {
            preferencesUtils = PreferencesUtils.getPreferences(paramContext);
        }
        return preferencesUtils;
    }

    /**
     * 根据角标获取
     *
     * @param index
     * @return
     */
    public static boolean getBoolean(int index) {
        return preferencesUtils.getBoolean((ConfigName + index));
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @param value
     */
    public static void setBoolean(int index, boolean value) {
        preferencesUtils.put((ConfigName + index), value, ParamType.BOOLEAN);
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @return
     */
    public static int getInt(String index) {
        return preferencesUtils.getInt((ConfigName + index));
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @param value
     */
    public static void setInt(String index, int value) {
        preferencesUtils.put((ConfigName + index), value, ParamType.INT);
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @return
     */
    public static int getLong(int index) {
        return preferencesUtils.getInt((ConfigName + index));
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @param value
     */
    public static void setLong(int index, int value) {
        preferencesUtils.put((ConfigName + index), value, ParamType.LONG);
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @return
     */
    public static String getString(int index) {
        return preferencesUtils.getString((ConfigName + index));
    }

    /**
     * 根据角标设置
     *
     * @param index
     * @param value
     */
    public static void setString(int index, String value) {
        preferencesUtils.put((ConfigName + index), value, ParamType.STRING);
    }

    public static String getString(String key) {
        return preferencesUtils.getString(key);
    }

    public static void setString(String key, String value) {
        preferencesUtils.put(key, value, ParamType.STRING);
    }
}
