package com.mrchen.simpleh5andorid.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.widget.Toast;

import com.mrchen.simpleh5andorid.App;

import java.util.Set;

public class PreferencesUtils {
    private static String DEFAULT_NAME = ConfigName.DEFAULT_NAME;
    private static PreferencesUtils preferences = null;
    private Context applicationContext = null;

    private String DEF_VALUE_STR = "";//string默认返回
    private int DEF_VALUE_NUMBER = -1;//int,long等默认返回
    private boolean DEF_VALUE_BOOLEAN = false;//boolena默认返回

    /**
     * 上下文对象!!一定要用applicationContext
     *
     * @param context
     */
    private PreferencesUtils(Context context) {
        if (null == applicationContext) {
            applicationContext = context;
        }
    }

    private PreferencesUtils(Context context, String name) {
        if (null == applicationContext)
            applicationContext = context;
        this.DEFAULT_NAME = name;
    }

    public static PreferencesUtils getPreferences(Context context) {
        if (null == preferences) {
            synchronized (PreferencesUtils.class) {
                if (null == preferences) {
                    preferences = new PreferencesUtils(context);
                }
            }
        }
        return preferences;
    }

    private SharedPreferences getPreference() {
        return applicationContext.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    }

    @SuppressLint("NewApi")
    public void put(String key, Object value, ParamType type) {
        Editor editor = getPreference().edit();
        switch (type) {
            case STRING:
                editor.putString(key, null != value ? value.toString() : "");
                break;
            case INT:
                editor.putInt(key, Integer.parseInt(value.toString()));
                break;
            case LONG:
                editor.putLong(key, Long.parseLong(value.toString()));
                break;
            case FLOAT:
                editor.putFloat(key, Float.parseFloat(value.toString()));
                break;
            case BOOLEAN:
                editor.putBoolean(key, Boolean.parseBoolean(value.toString()));
                break;
            case SET:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    editor.putStringSet(key, (Set<String>) value);
                }
            default:
                break;
        }
        editor.commit();
    }

    public String getString(String key) {
        return getPreference().getString(key, DEF_VALUE_STR);
    }

    public int getInt(String key) {
        return getPreference().getInt(key, DEF_VALUE_NUMBER);
    }

    public float getFloat(String key) {
        return getPreference().getFloat(key, DEF_VALUE_NUMBER);
    }

    public long getLong(String key) {
        return getPreference().getLong(key, DEF_VALUE_NUMBER);
    }

    public boolean getBoolean(String key) {
        return getPreference().getBoolean(key, DEF_VALUE_BOOLEAN);
    }

    public void putObject(String key, Object val) {
        if (val instanceof String)
            put(key, val.toString(), ParamType.STRING);
        else if (val instanceof Integer)
            put(key, val.toString(), ParamType.INT);
        else if (val instanceof Boolean)
            put(key, val.toString(), ParamType.BOOLEAN);
        else if (val instanceof Long)
            put(key, val.toString(), ParamType.LONG);
        else if (val instanceof Float)
            put(key, val.toString(), ParamType.FLOAT);
        else
        Toast.makeText(App.getContext(),"不识别类型，保存失败",Toast.LENGTH_SHORT).show();
    }
    public void deleteKey(String key){
        Editor editor = getPreference().edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * SharedPreferences 获取内容
     * int long float 默认 -1 ,boolean默认返回false，String默认返回null
     *
     * @param key
     * @param classType
     * @return
     */
    public Object getObject(String key, Class<?> classType) {
        if (String.class.isAssignableFrom(classType))
            return getString(key);
        else if (Boolean.class.isAssignableFrom(classType))
            return getBoolean(key);
        else if (Integer.class.isAssignableFrom(classType))
            return getInt(key);
        else if (Long.class.isAssignableFrom(classType))
            return getLong(key);
        else if (Float.class.isAssignableFrom(classType))
            return getFloat(key);
        return null;
    }

    /**
     * 删除数据
     *
     * @param key
     */
    public void removeObject(String key) {
        Editor editor = getPreference().edit();
        editor.remove(key);
        editor.commit();
    }

    @SuppressLint("NewApi")
    public Set<String> getSet(String key) {
        Set<String> set = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            set = getPreference().getStringSet(key, null);
        }
        return set;
    }

    /**
     * 存储参数类型和操作类型
     */
    public enum ParamType {
        STRING, INT, LONG, FLOAT, BOOLEAN, SET
    }
}
