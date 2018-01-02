package com.cai.easyuse.app;

import com.cai.easyuse.R;
import com.cai.easyuse.util.LogUtils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * AlertDialog对话框
 * <p>
 * Created by cailingxiao on 2017/2/11.
 */
public abstract class BuiAlertDialog extends Dialog {
    private static final String TAG = "BuiALertDialog";

    /**
     * 根布局
     */
    private View mRoot = null;

    public BuiAlertDialog(Context context, int layoutId) {
        super(context, R.style.BuiDialogTheme);
        mRoot = LayoutInflater.from(context).inflate(layoutId, null, false);
        setContentView(mRoot);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics();
        lp.width = d.widthPixels;
        lp.height = d.heightPixels;
        //        dialogWindow.setWindowAnimations(R.style.BuiDialogAnim);
        dialogWindow.setAttributes(lp);
        initView(mRoot);
    }

    protected View getRoot() {
        return mRoot;
    }

    /**
     * 初始化view
     *
     * @param root
     */
    protected abstract void initView(View root);

    /**
     * 获取view
     *
     * @param id
     * @param <T>
     *
     * @return
     */
    protected <T extends View> T findUi(int id) {
        return (T) mRoot.findViewById(id);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
        }
    }
}
