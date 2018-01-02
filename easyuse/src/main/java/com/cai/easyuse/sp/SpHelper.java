package com.cai.easyuse.sp;

import java.util.Map;

import com.cai.easyuse.util.ContextUtils;
import com.cai.easyuse.util.OsVerUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

/**
 * 对perference处理的工具类
 *
 * @author cailingxiao
 * @date 2016年3月4日
 */
public final class SpHelper {
    private static final String TAG = "PreferenceUtils";

    private static final String DEFAULT_PREF_NAME = "dft_pref";
    private static final Object LOCKED = new Object();
    /**
     * 单例
     */
    private static volatile SpHelper sInstance = null;
    /**
     * sharedPreferenced持有
     */
    private SharedPreferences mSp;
    private Editor mEditor;

    private SpHelper(Context ctx) {
        if (null == ctx) {
            throw new IllegalArgumentException("不能使用空的context初始化");
        }
        mSp = ctx.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    /**
     * 使用BuiApplication初始化的单例，无区别
     * <p>
     * 注意集成BuiApplication才能使用该方法，否者使用
     *
     * @return
     */
    public static SpHelper getInstance() {
        return getInstance(null);
    }

    /**
     * 使用指定ctx初始化的单例，无区别
     *
     * @param ctx
     *
     * @return
     */
    public static SpHelper getInstance(Context ctx) {
        if (null == sInstance) {
            synchronized(LOCKED) {
                if (null == sInstance) {
                    final Context usedCtx = null != ctx ? ctx.getApplicationContext() : ContextUtils.getContext();
                    sInstance = new SpHelper(usedCtx);
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取SharedPreference
     *
     * @return
     */
    public SharedPreferences getPref() {
        return mSp;
    }

    /**
     * 获取所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return mSp.getAll();
    }

    /**
     * 查看是否包含某键
     *
     * @param key
     *
     * @return
     */
    public boolean contains(String key) {
        return mSp.contains(key);
    }

    /**
     * 获取key对应的值，不存在返回defValue
     *
     * @param key
     * @param defValue
     *
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    /**
     * 获取key对应的值，不存在返回defValue
     *
     * @param key
     * @param defValue
     *
     * @return
     */
    public float getFloat(String key, float defValue) {
        return mSp.getFloat(key, defValue);
    }

    /**
     * 获取key对应的值，不存在返回defValue
     *
     * @param key
     * @param defValue
     *
     * @return
     */
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    /**
     * 获取key对应的值，不存在返回defValue
     *
     * @param key
     * @param defValue
     *
     * @return
     */
    public long getLong(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    /**
     * 获取key对应的值，不存在返回defValue
     *
     * @param key
     * @param defValue
     *
     * @return
     */
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    /**
     * 存储对应的键值对
     *
     * @param key
     * @param b
     *
     * @return
     */
    public boolean putBoolean(String key, boolean b) {
        mEditor.putBoolean(key, b);
        return commit();
    }

    /**
     * 存储对应的键值对
     *
     * @param key
     * @param i
     *
     * @return
     */
    public boolean putInt(String key, int i) {
        mEditor.putInt(key, i);
        return commit();
    }

    /**
     * 存储对应的键值对
     *
     * @param key
     * @param f
     *
     * @return
     */
    public boolean putFloat(String key, float f) {
        mEditor.putFloat(key, f);
        return commit();
    }

    /**
     * 存储对应的键值对
     *
     * @param key
     * @param l
     *
     * @return
     */
    public boolean putLong(String key, long l) {
        mEditor.putLong(key, l);
        return commit();
    }

    /**
     * 存储对应的键值对
     *
     * @param key
     * @param s
     *
     * @return
     */
    public boolean putString(String key, String s) {
        mEditor.putString(key, s);
        return commit();
    }

    /**
     * 移除一个键
     *
     * @param key
     *
     * @return
     */
    public boolean removeKey(String key) {
        mEditor.remove(key);
        return commit();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private boolean commit() {
        if (OsVerUtils.hasHoneycomb()) {
            mEditor.apply();
            return true;
        } else {
            return mEditor.commit();
        }
    }

}
