package com.cai.easyuse.widget.dialog;

import android.content.Context;
import android.view.View;

import com.cai.easyuse.app.BuiAlertDialog;

/**
 * 基本对话框实现，展示两个按钮和一个信息展示区域的对话框
 * <p>
 * Created by cailingxiao on 2017/8/19.
 */
public class NormalDialog extends BuiAlertDialog {
    public NormalDialog(Context context) {
        super(context, 0);
    }

    @Override
    protected void initView(View root) {

    }
}
