package com.io.cailingxiao.easyusetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cai.easyuse.app.BuiActivity;
import com.cai.easyuse.base.mark.BuiCallback;
import com.cai.easyuse.http.AsyncHttpApi;
import com.cai.easyuse.util.ContextUtils;
import com.cai.easyuse.util.LogUtils;

import butterknife.BindView;

public class MainActivity extends BuiActivity {

    @BindView(R.id.button)
    Button mBtSubmit;

    @BindView(R.id.textView)
    TextView mTvInfo;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    protected void initView() {
        ContextUtils.setContext(getApplication());
        LogUtils.setDebugable();
    }

    protected void initData() {
        AsyncHttpApi.getInstance().asyncGet("http://www.zaixiandeng.cn", new BuiCallback() {
            @Override
            public void onSuccess(int statusCode, Object data) {
                LogUtils.e("resp=" + data);

            }

            @Override
            public void onFail(int statusCode, Object error) {
                LogUtils.e(error);
            }
        });
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "bind data", Toast.LENGTH_SHORT).show();
            }
        });
        mTvInfo.setText("okokiojasjdfjadslfja");

        button2.setText("bind auto");

    }
}
