package com.cai.easyuse.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cai.easyuse.R;
import com.cai.easyuse.base.IPresenter;
import com.cai.easyuse.base.IView;
import com.cai.easyuse.event.EventApi;
import com.cai.easyuse.language.LanApi;
import com.cai.easyuse.util.DeviceUtils;
import com.cai.easyuse.util.SystemBarTintManager;
import com.cai.easyuse.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.cai.easyuse.event.EventConstant.EVENT_ID_CHANGE_LANGUAGE_CN;
import static com.cai.easyuse.event.EventConstant.EVENT_ID_CHANGE_LANGUAGE_EN;
import static com.cai.easyuse.event.EventConstant.EVENT_ID_FINISH_OTHER;

/**
 * 基本的activity类
 *
 * @author cailingxiao
 */
public abstract class BuiActivity extends FragmentActivity implements EventApi.IBuiEvent, IView {
    protected SystemBarTintManager mTintManager;
    protected View mRoot;
    private boolean mIsFront = false;
    private SparseArray<View> mSparse = new SparseArray<View>();
    private View mLoadingView;
    private boolean mIsTranslucent = false;

    private Unbinder mUnBinder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        getWindow().getDecorView()
        //                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        if (OsVerUtils.hasKitKat()) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        mRoot = LayoutInflater.from(this).inflate(getLayoutId(), null, false);
        setContentView(mRoot);

        mUnBinder = ButterKnife.bind(this);

        initView();
        settingTranslucent();
        // 界面加载好后再进行数据请求，避免数据请求完成后界面没加载好
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        EventApi.registerEvent(this);
    }

    private void settingTranslucent() {
        if (mIsTranslucent && Build.VERSION.SDK_INT >= 19) {
            DeviceUtils.setTranslucentStatus(true, this);
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setStatusBarTintResource(R.color.colorPrimary);
            mTintManager.setStatusBarDarkMode(false, this);
        }
    }

    /**
     * 获取当前页面的presenter，可能为空
     *
     * @return
     */
    protected IPresenter getPresenter() {
        return null;
    }

    /**
     * 一定要返回一个布局的id，这就是页面的布局#setContentView会调用这个
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 做一些初始化工作,每个activity创建一次调用一次，同#onCreate
     */
    protected abstract void initView();

    /**
     * 做一些数据初始化的工作，会自动采用延缓加载策略
     */
    protected abstract void initData();

    /**
     * 显示loading
     */
    @Override
    @UiThread
    public synchronized void showLoading() {
        if (null == mLoadingView) {
            mLoadingView = LayoutInflater.from(this).inflate(R.layout.bui_loading, null);
            getWindow().addContentView(mLoadingView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mLoadingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
        mLoadingView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏loading
     */
    @Override
    @UiThread
    public synchronized void hideLoading() {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * loading界面是否显示在界面上
     *
     * @return
     */
    @UiThread
    public boolean isShowingLoading() {
        return null != mLoadingView && mLoadingView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void toast(String msg) {
        ToastUtils.showToast(this, msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsFront = false;
    }

    @Override
    protected void onDestroy() {
        synchronized (mSparse) {
            mSparse.clear();
        }
        EventApi.unregisterEvent(this);
        if (null != getPresenter()) {
            getPresenter().release();
        }
        if (null != mUnBinder) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    /**
     * 是否在与用户前端可交互
     *
     * @return
     */
    public boolean isOnFront() {
        return mIsFront;
    }

    /**
     * 通过resId找到布局里面的view，效果同{@link Activity#findViewById(int)}，不需要进行类型转换
     *
     * @param resId
     * @return
     */
    protected <T extends View> T findUi(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 使用sparseArray进行存储view的策略，这样做可以不需要定义具体的id名称
     *
     * @param resId
     * @return
     */
    protected <T extends View> T find(int resId) {
        //        return findUi(resId);
        View view = mSparse.get(resId);
        if (null == view) {
            view = findUi(resId);
            if (null != view) {
                mSparse.put(resId, view);
            }
        }
        return (T) view;
    }

    @Override
    public void onEvent(EventApi.BuiEvent event) {
        if (null != event) {
            switch (event.eventId) {
                case EVENT_ID_CHANGE_LANGUAGE_CN:
                    LanApi.getInstance().setLang(EVENT_ID_CHANGE_LANGUAGE_CN);
                    recreate();
                    break;
                case EVENT_ID_CHANGE_LANGUAGE_EN:
                    LanApi.getInstance().setLang(EVENT_ID_CHANGE_LANGUAGE_EN);
                    recreate();
                    break;
                case EVENT_ID_FINISH_OTHER:
                    if (!getClass().getName().equals(event.eventObj + "")) {
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
