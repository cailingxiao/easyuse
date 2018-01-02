package com.cai.easyuse.language;

import static com.cai.easyuse.event.EventConstant.EVENT_ID_CHANGE_LANGUAGE_CN;

import java.util.Locale;

import com.cai.easyuse.event.EventConstant;
import com.cai.easyuse.language.core.LanModel;
import com.cai.easyuse.util.ResUtils;

import android.content.Context;

/**
 * 语言切换api
 * <p>
 * Created by cailingxiao on 2017/2/6.
 */
public final class LanApi {

    private static volatile LanApi sInstance = null;

    private LanModel mModel;

    private LanApi() {
        mModel = new LanModel();
    }

    public static LanApi getInstance() {
        if (null == sInstance) {
            synchronized (LanApi.class) {
                if (null == sInstance) {
                    sInstance = new LanApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化语言相关，设置语言,在application中设置就行
     *
     * @param context
     */
    public void init(Context context) {
        int now = mModel.getNowLang();
        if (-1 != now) {
            // 自己有设置
            changeLang(now);
        }
    }

    /**
     * 修改语言属性
     *
     * @param lan {@link EventConstant#EVENT_ID_CHANGE_LANGUAGE_EN}, {@link EventConstant#EVENT_ID_CHANGE_LANGUAGE_CN}
     */
    private void changeLang(int lan) {
        if (EventConstant.EVENT_ID_CHANGE_LANGUAGE_CN == lan) {
            // 中文
            ResUtils.changeLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if (EventConstant.EVENT_ID_CHANGE_LANGUAGE_EN == lan) {
            // 英文
            ResUtils.changeLanguage(Locale.ENGLISH);
        }
    }

    /**
     * 设置语言
     *
     * @param lan {@link EventConstant#EVENT_ID_CHANGE_LANGUAGE_EN}, {@link EventConstant#EVENT_ID_CHANGE_LANGUAGE_CN}
     */
    public void setLang(int lan) {
        mModel.setNowLang(lan);
        changeLang(lan);
    }

    /**
     * 获取当前语言
     *
     * @return
     */
    public int getNowLang() {
        return mModel.getNowLang();
    }

    /**
     * 是否使用中文
     *
     * @return
     */
    public boolean isChinese() {
        return EVENT_ID_CHANGE_LANGUAGE_CN == getNowLang();
    }
}
