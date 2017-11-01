package com.cai.easyuse.app;


import com.cai.easyuse.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

/**
 * 基本的对话框类
 *
 * @author cailingxiao
 *
 */
public abstract class BuiDialog extends DialogFragment {
    private View mRootView;

    // @Override
    // public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // mRootView = inflater.inflate(getLayoutId(), container, false);
    // initView(mRootView);
    // return mRootView;
    // }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BuiDialogTheme);
        mRootView = View.inflate(getActivity(), getLayoutId(), null);
        dialog.setContentView(mRootView);
        initView(mRootView);
        getDialog().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        return dialog;
    }

    /**
     * 将runnable送到主线程中运行
     *
     * @param action
     */
    protected void postUi(Runnable action) {
        if (null != getActivity()) {
            getActivity().runOnUiThread(action);
        }
    }

    /**
     * 通过resId找到对应的view
     *
     * @param resId
     * @return
     */
    protected <T extends View> T find(int resId) {
        return (T) mRootView.findViewById(resId);
    }

    /**
     * 获取基本的view
     *
     * @return
     */
    protected View getRootView() {
        return mRootView;
    }

    /**
     * 获取这个对话框的布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 进行初始化操作
     *
     * @param rootView 根布局
     */
    protected abstract void initView(View rootView);

    /**
     * 做一些数据初始化的工作，会自动采用延缓加载策略
     */
    protected abstract void initData();
}
