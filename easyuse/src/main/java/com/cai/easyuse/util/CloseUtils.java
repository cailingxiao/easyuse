package com.cai.easyuse.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * 安全关闭可关闭的流
 *
 * @author cailingxiao
 * @date 2016年2月18日
 */
public final class CloseUtils {

    private CloseUtils() {

    }

    /**
     * 关闭
     *
     * @param closeables
     */
    public static void close(Closeable... closeables) {
        if (null != closeables) {
            for (Closeable closeable : closeables) {
                try {
                    if (null != closeable) {
                        closeable.close();
                    }
                } catch (Throwable ignored) {
                    if (LogUtils.isDebug()) {
                        ignored.printStackTrace();
                    }
                }
            }
            closeables = null;
        }
    }

    /**
     * 关闭，顺便进行flush操作
     *
     * @param closeable
     */
    public static void closeWithFlush(Closeable closeable) {
        if (null != closeable && closeable instanceof Flushable) {
            try {
                ((Flushable) closeable).flush();
            } catch (IOException e) {
                if (LogUtils.isDebug()) {
                    e.printStackTrace();
                }
            }
        }
        close(closeable);
    }

}
