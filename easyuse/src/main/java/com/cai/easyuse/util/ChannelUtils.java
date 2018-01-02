package com.cai.easyuse.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 批量打包提取渠道号工具
 * 
 * @author cailingxiao
 * @date 2016年4月12日
 * 
 */
public final class ChannelUtils {
    private static final String TAG = "MarketUtils";
    private static String sChannel = null;

    /**
     * 默认渠道
     */
    private static final String DEFAULT_CHANNEL = "develop";
    private static final String CHANNEL_PRE = "META-INF/buichannel_";
    private static final boolean USE_AUTO_PKG = false;

    private ChannelUtils(Context context) {
    }

    /**
     * 设置渠道号
     *
     * @param channel
     */
    public static void setChannel(String channel) {
        sChannel = channel;
    }

    /**
     * 获取渠道
     * 
     * @return
     */
    public static String getChannel() {
        if (null == sChannel) {
            sChannel = getChannel(ContextUtils.getContext());
        }
        return sChannel;
    }

    /**
     * 具体获取渠道的方法
     * 
     * @param context
     * @return
     */
    private static String getChannel(Context context) {
        if (!USE_AUTO_PKG) {
            return DEFAULT_CHANNEL;
        }
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(CHANNEL_PRE)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            LogUtils.e(TAG, e.getMessage());
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            return ret.substring(split[0].length() + 1);
        } else {
            return DEFAULT_CHANNEL;
        }
    }
}
