package com.cai.easyuse.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cai.easyuse.util.AssetUtils;

/**
 * 全局统一配置参数获取
 * <p>
 * Created by cailingxiao on 2017/3/23.
 */
public class EasyConfigApi {
    public static final String CONFIG_NAME = "config.properties";
    private Properties mProperties;
    private static volatile EasyConfigApi sInstance = null;

    private EasyConfigApi() {
        mProperties = new Properties();
        try {
            InputStream is = AssetUtils.getAssets().open(CONFIG_NAME);
            mProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static EasyConfigApi getInstance() {
        if (null == sInstance) {
            synchronized (EasyConfigApi.class) {
                if (null == sInstance) {
                    sInstance = new EasyConfigApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取配置参数
     *
     * @param key
     * @param defaultValue
     *
     * @return
     */
    public String getProp(String key, String defaultValue) {
        return mProperties.getProperty(key, defaultValue);
    }

    /**
     * 获取配置参数
     *
     * @param key
     *
     * @return
     */
    public String getProp(String key) {
        return getProp(key, "");
    }
}
