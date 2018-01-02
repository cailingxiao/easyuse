package com.cai.easyuse.hybrid.constant;

/**
 * 网页通信参数
 * <p>
 * 通信实例： h5页面通过Prompt传递过来的msg的内容如下(这是一个调用NA端计算加法的请求)：
 * <p>
 * {"method":"plus","actionId":1100,"args":{"members":["1","2","3"]},"callback":"plus_callback"}
 * <p>
 * 例如计算结果为6，NA端给H5页面注入代码如下：
 * <p>
 * webview.loadUrl("javascript:plus_callback(actionId,6);");
 * 
 * 
 * @author cailingxiao
 * @date 2016年3月3日
 * 
 */
public final class BuiWebConstant {

    public static final String JSON_METHOD = "method"; // 方法调用名
    public static final String JSON_ACTIONID = "actionId"; // 行为唯一识别号，用来区分同个方法的不同的调用
    public static final String JSON_ARGS = "args"; // 方法调用参数
    public static final String JSON_CALLBACK = "callback"; // 方法调用回调方法

}