package com.cai.easyuse.language.core;

import static com.cai.easyuse.event.EventConstant.EVENT_ID_CHANGE_LANGUAGE_CN;

import com.cai.easyuse.sp.SpHelper;

/**
 * Created by cailingxiao on 2017/2/6.
 */
public final class LanModel {
    private static final String NOW_LANG = "now_lang";

    public LanModel() {

    }

    /**
     * 获取当前语言
     *
     * @return
     */
    public int getNowLang() {
        return SpHelper.getInstance().getInt(NOW_LANG, EVENT_ID_CHANGE_LANGUAGE_CN);
    }

    /**
     * 设置当前语言
     *
     * @param lan
     */
    public void setNowLang(int lan) {
        SpHelper.getInstance().putInt(NOW_LANG, lan);
    }
}
