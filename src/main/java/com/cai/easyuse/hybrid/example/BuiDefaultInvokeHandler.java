package com.cai.easyuse.hybrid.example;

import org.json.JSONObject;

import com.cai.easyuse.hybrid.annotation.H5CallNa;

import android.content.Context;

/**
 * 处理H5发起的请求的类的例子
 * <p>
 * 请仿照这个文件写出方法，方法名不重要，请按照注解配置,方法一定要有这两个参数
 * <p>
 * 将处理请求的类的全名（包名+类名）写在assets根目录下的handler文件中
 * <p>
 * {"defaultHandler":"com.cai.easyuse.hybrid.example.BuiDefaultInvokeHandler"}
 * 其中，com.cai.easyuse.hybrid.example.BuiDefaultInvokeHandler就是处理类
 * 
 * @author cailingxiao
 * @date 2016年3月3日
 * 
 */
public final class BuiDefaultInvokeHandler {
    private static final String TAG = "BuiDefaultInvokeHandler";

    public BuiDefaultInvokeHandler() {

    }

    @H5CallNa(invokeName = "default")
    public String doExample1(Context ctx, JSONObject args) {
        System.out.println("this is the default handle from " + TAG);
        return "default" + TAG;
    }

    @H5CallNa(invokeName = "test")
    public String doExample(Context ctx, JSONObject args) {
        System.out.println("please use  a new file to handle request!" + TAG);
        return "test" + TAG;
    }
}
