package com.cai.easyuse.widget.countdown;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by lingxiaocai on 2017/4/28.
 */

public abstract class CountDownTimerExt {
    private static final String TAG = "CountDownTimeExt";
    private static final int MSG = 1;

    private long mTotalMilliSeconds;
    private long mIntervalMilliSeconds;

    private long mTimeLeft;

    private boolean mPaused = false;
    private boolean mFinished = false;

    public CountDownTimerExt(long totalMilliSeconds, long intervalMilliSeconds) {
        mTotalMilliSeconds = totalMilliSeconds;
        mIntervalMilliSeconds = intervalMilliSeconds;
        mTimeLeft = mTotalMilliSeconds;
    }

    public void reset() {
        pause();
        mTimeLeft = mTotalMilliSeconds;
    }

    protected abstract void onFinish();

    protected abstract void onTick(long milliSecondsLeft, long totalMilliSeconds);

    public synchronized void start() {
        if (mTimeLeft <= 0) {
            mFinished = true;
            onFinish();
            return;
        }
        mFinished = false;
        mPaused = false;
        mHandler.sendEmptyMessage(MSG);
    }

    public void cancel() {
        pause();
        onFinish();
    }

    public void pause() {
        mPaused = true;
        mHandler.removeMessages(MSG);
    }

    public void resume() {
        mPaused = false;
        mHandler.removeMessages(MSG);
        mHandler.sendEmptyMessageDelayed(MSG, mIntervalMilliSeconds);
    }

    public boolean isFinished() {
        return mFinished;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (MSG == msg.what) {
                if (mPaused || mFinished) {
                    return;
                }
                if (mTimeLeft <= 0) {
                    mFinished = true;
                    onFinish();
                    return;
                }
                mTimeLeft -= mIntervalMilliSeconds;
                onTick(mTimeLeft, mTotalMilliSeconds);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mIntervalMilliSeconds);
                return;
            }
            super.handleMessage(msg);
        }
    };
}
