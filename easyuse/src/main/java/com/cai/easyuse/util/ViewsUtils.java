package com.cai.easyuse.util;

import android.view.View;

/**
 * view常用工具
 * <p/>
 * Created by cailingxiao on 2017/3/8.
 */
public final class ViewsUtils {
    private ViewsUtils() {

    }

    public static void enable(View... views) {
        if (null != views) {
            for (View view : views) {
                if (!view.isEnabled()) {
                    view.setEnabled(true);
                }
            }
        }
    }

    public static void disEnable(View... views) {
        if (null != views) {
            for (View view : views) {
                if (view.isEnabled()) {
                    view.setEnabled(false);
                }
            }
        }
    }

    /**
     * 使view可见
     *
     * @param views
     */
    public static void visible(View... views) {
        if (null != views) {
            for (View view : views) {
                if (null != view && view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 使view不可见
     *
     * @param views
     */
    public static void gone(View... views) {
        if (null != views) {
            for (View view : views) {
                if (null != view && view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 使view不可见
     *
     * @param views
     */
    public static void invisible(View... views) {
        if (null != views) {
            for (View view : views) {
                if (view.getVisibility() != View.INVISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 一次性添加clicklistener
     *
     * @param listener
     * @param views
     */
    public synchronized static void setOnClickListener(View.OnClickListener listener, View... views) {
        if (null != views && null != listener) {
            for (View view : views) {
                view.setOnClickListener(listener);
            }
        }
    }

    /**
     * 在指定的view上查找id对应的view
     *
     * @param container
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends View> T findView(View container, int id) {
        return (T) container.findViewById(id);
    }
}
