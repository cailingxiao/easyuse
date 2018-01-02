package com.cai.easyuse.hybrid;

import com.cai.easyuse.hybrid.core.BuiWebChromeClient;
import com.cai.easyuse.hybrid.core.BuiWebViewClient;

/**
 * 类似于结构体的东西，用来方便管理两个类
 * 
 * @author cailingxiao
 * @date 2016年3月3日
 * 
 */
public class BuiWebViewStruct {
    /**
     * webviewclient
     */
    public BuiWebViewClient webViewClient = null;
    /**
     * webchromeclient
     */
    public BuiWebChromeClient webChromeClient = null;
}