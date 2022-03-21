package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG_LOG = SplashActivity.class.getName();
    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 2000L;
    private static final int GO_AHEAD_WHAT = 1;
    private long mStartTime = -1L;
    private boolean mIsDone;

    private static class UiHandler extends Handler {
        private final WeakReference<SplashActivity> mActivityRef;

        public UiHandler(final SplashActivity srcActivity) {
            this.mActivityRef = new WeakReference<>
                    (srcActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SplashActivity srcActivity = this.mActivityRef.get();
            if (srcActivity == null) {
                Log.d(TAG_LOG, "Reference to NoLeakSplashActivity lost!");
                return;
            }

            long elapsedTime = SystemClock.uptimeMillis() -
                    srcActivity.mStartTime;
            if (elapsedTime >= MIN_WAIT_INTERVAL && !srcActivity.mIsDone) {
                srcActivity.mIsDone = true;
                srcActivity.nextActivity();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

// We initialize the Handler
        UiHandler mHandler = new UiHandler(this);

        if (mStartTime == -1L) {
            mStartTime = SystemClock.uptimeMillis();
        }
        final Message goAheadMessage = mHandler.obtainMessage(GO_AHEAD_WHAT);
        mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
        Log.d(TAG_LOG, "Handler message sent!");


    }


    private void nextActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}