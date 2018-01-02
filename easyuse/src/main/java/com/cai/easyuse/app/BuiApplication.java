package com.cai.easyuse.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.cai.easyuse.util.ContextUtils;

/**
 * 基本的application类
 *
 * @author cailingxiao
 */
public abstract class BuiApplication extends Application {
    private static final String TAG = "BuiApplication";
    private static Context sContext = null;

    /**
     * 如果不想继承BuiApplication，可以使用该静态方法来使用相关的类
     *
     * @param context
     */
    public static final void initWithOutInstance(Context context) {
        if (null == sContext) {
            sContext = context.getApplicationContext();
        }
    }

    /**
     * 获得application单例
     *
     * @return
     */
    public static final Context getAppContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        ContextUtils.setContext(this);
        init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化单例
     */
    private void init(Context context) {
        doInit();
        initWithOutInstance(context);

        if (getPackageName().equals(ContextUtils.getCurrentProcess(getAppContext()))) {
            doInitOnce();
        }
    }

    /**
     * 每个application只会初始化一次,进行初始化操作，如：LogUtils的开关等
     */
    protected abstract void doInit();

    /**
     * 在多进程中只执行一次的初始化
     */
    protected void doInitOnce() {

    }

}
