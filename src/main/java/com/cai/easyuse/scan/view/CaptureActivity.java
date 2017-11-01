package com.cai.easyuse.scan.view;

import java.io.IOException;
import java.util.Vector;

import com.cai.easyuse.R;
import com.cai.easyuse.scan.ScanConst;
import com.cai.easyuse.scan.camera.CameraManager;
import com.cai.easyuse.scan.decoding.CaptureActivityHandler;
import com.cai.easyuse.scan.decoding.InactivityTimer;
import com.cai.easyuse.util.LogUtils;
import com.cai.easyuse.util.PermissionUtils;
import com.cai.easyuse.util.ToastUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {
    private static final String TAG = "CaptureActivity";

    public static final int REQ_CODE = 1234;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private TextView mTvTopTip;
    private TextView mTvBottomTip;
    private SurfaceHolder mHolder = null;
    private boolean isReq = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        mTvTopTip = (TextView) findViewById(R.id.ac_top_tip);
        mTvBottomTip = (TextView) findViewById(R.id.ac_bottom_tip);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        Intent i = getIntent();
        if (null != i) {
            String tip = i.getStringExtra(ScanConst.PARAMS_TOP_TIP);
            if (!TextUtils.isEmpty(tip)) {
                mTvTopTip.setText(tip);
            }
            tip = i.getStringExtra(ScanConst.PARAMS_BOTTOM_TIP);
            if (!TextUtils.isEmpty(tip)) {
                mTvBottomTip.setText(tip);
            }
        }

        handleNoPermission();
        addTouchEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        mHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(mHolder);
        } else {
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        LogUtils.e(ScanConst.MODULE_TAG, "#onPause");
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        LogUtils.e(ScanConst.MODULE_TAG, "#onDestroy");
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * ����ɨ����
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        LogUtils.e(ScanConst.MODULE_TAG, "#handleDecode");
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        LogUtils.e(TAG, "scan result=" + resultString);
        if (resultString.equals("")) {
            Toast.makeText(this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(ScanConst.DATA_RESULT, resultString);
            // bitmap is too big
            // bundle.putParcelable(ScanConst.DATA_BITMAP, barcode);
            if (null != barcode && !barcode.isRecycled()) {
                barcode.recycle();
            }
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    private void handleNoPermission() {
        if (!PermissionUtils.hasPermission(this, Manifest.permission.CAMERA)) {
            ToastUtils.showToast(R.string.scan_permission_tip);
            PermissionUtils.requestPermission(this, Manifest.permission.CAMERA, REQ_CODE);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
        LogUtils.e(ScanConst.MODULE_TAG, "#surfaceDestroyed");

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private void addTouchEvent() {
        mTvBottomTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNoPermission();
            }
        });
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}