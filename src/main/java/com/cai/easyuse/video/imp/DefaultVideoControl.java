package com.cai.easyuse.video.imp;

import com.cai.easyuse.R;
import com.cai.easyuse.util.ConvertUtils;
import com.cai.easyuse.video.base.AbsVideoControl;
import com.cai.easyuse.video.base.IVideo;
import com.cai.easyuse.video.base.OnVideoListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 视频播放器
 * <p>
 * Created by cailingxiao on 2017/3/13.
 */
public class DefaultVideoControl extends AbsVideoControl {
    private static final String TAG = "DefaultVideoControl";
    private static final String[] SPEEDS = new String[] {"0.8", "1.0", "1.4", "1.8", "2.0"};
    private static final long DELAYED_TIME = 60;

    private View mContainer;
    private View mBack;
    private TextView mTvTitle;
    private TextView mTvSpeed;
    private TextView mTvCurTime;
    private TextView mTvTotalTime;
    private ImageButton mIbPlayPause;
    private SeekBar mSbProgress;

    private boolean isUpdating = true;

    /**
     * 计时器
     */
    private Handler mTimerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // 定时更新页面计时

        }
    };

    /**
     * 更新界面任务
     */
    private Runnable mUpdateTask = new Runnable() {
        @Override
        public void run() {
            updateUi();
            if (isUpdating) {
                mTimerHandler.postDelayed(mUpdateTask, DELAYED_TIME);
            } else {
                mTimerHandler.removeCallbacks(mUpdateTask);
            }
        }
    };
    /**
     * 视频播放情况
     */
    private OnVideoListener mVideoListener = new OnVideoListener() {
        @Override
        public void onStart() {
            mTimerHandler.post(new Runnable() {
                @Override
                public void run() {
                    // 初始化控制界面的数据
                    mTvCurTime.setText(convertTime2Str(mVideo.getCurrentPosition()));
                    mTvTotalTime.setText(convertTime2Str(mVideo.getDuration()));
                }
            });

        }

        @Override
        public void onPlayOver() {

        }

        @Override
        public void onPlayError() {

        }
    };

    public DefaultVideoControl(Context context) {
        super(context);
    }

    public DefaultVideoControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultVideoControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DefaultVideoControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.default_video_control;
    }

    @Override
    protected View initView(View root) {
        mContainer = findUi(R.id.dvc_container);
        mBack = findUi(R.id.dvc_back_icon);
        mTvTitle = findUi(R.id.dvc_title);
        mTvSpeed = findUi(R.id.dvc_speed);
        mTvCurTime = findUi(R.id.dvc_time_current);
        mTvTotalTime = findUi(R.id.dvc_time_total);
        mIbPlayPause = findUi(R.id.dvc_play_pause);
        mSbProgress = findUi(R.id.dvc_seekbar);
        initEvent();
        return root;
    }

    private void initEvent() {
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mBackClickListener) {
                    mBackClickListener.onClick(v);
                }
            }
        });
        mTvSpeed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed();
            }
        });
        mIbPlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放或者暂停
                if (mVideo.isPaused()) {
                    mIbPlayPause.setImageResource(R.mipmap.mediacontroller_pause);
                    mVideo.resume();
                } else {
                    mIbPlayPause.setImageResource(R.mipmap.mediacontroller_play);
                    mVideo.pause();
                }
            }
        });
        mSbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private boolean isFromUser = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                isFromUser = fromUser;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isFromUser) {
                    isFromUser = false;
                    if (null != mVideo) {
                        mVideo.seekTo(
                                (long) (1.0f * mVideo.getDuration() * seekBar.getProgress() / mSbProgress.getMax()));
                        callUpdate();
                    }
                }
            }
        });
        // 处理亮度、音量
        final GestureDetector gd = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        mContainer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
    }

    /**
     * 更新页面
     */
    private void updateUi() {
        if (null != mVideo) {
            mTvCurTime.setText(convertTime2Str(mVideo.getCurrentPosition()));
            mSbProgress.setProgress((int) (1.0f * mVideo.getCurrentPosition() / mVideo.getDuration() * mSbProgress
                    .getMax()));
        }
    }

    /**
     * 改变播放速度
     */
    private void changeSpeed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("播放速度").setSingleChoiceItems(SPEEDS, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setSpeed(ConvertUtils.str2Float(SPEEDS[which], 1.0f));
            }
        });
        builder.create().show();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    @Override
    public void setSpeed(float speed) {
        if (speed >= 0.8f && speed <= 2.0f) {
            mTvSpeed.setText(String.format("%.1f", speed));
            if (null != mVideo) {
                mVideo.setPlaybackSpeed(speed);
            }
        }
    }

    @Override
    public void setVideo(IVideo video) {
        super.setVideo(video);
        if (null != video) {
            mVideo.setOnVideoListener(mVideoListener);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        // 可见，打开计时；不可见，暂停计时
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 开始
        isUpdating = true;
        callUpdate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 停止
        isUpdating = false;
        mTimerHandler.removeCallbacks(mUpdateTask);
    }

    private void callUpdate() {
        mTimerHandler.removeCallbacks(mUpdateTask);
        mTimerHandler.post(mUpdateTask);
    }

    /**
     * 将毫秒数转化为合适的显示，一般为mm:ss
     *
     * @param time
     *
     * @return
     */
    private String convertTime2Str(long time) {
        int minute = (int) (time / 60000);
        int second = (int) (time / 1000 % 60);
        return minute + ":" + second;
    }
}
