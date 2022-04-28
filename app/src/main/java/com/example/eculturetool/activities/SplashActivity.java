package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG_LOG = SplashActivity.class.getName();
    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 2000L;
    private static final int GO_AHEAD_WHAT = 1;
    private long mStartTime = -1L;
    private boolean mIsDone;
    private Connection connection = new Connection();
    private DataBaseHelper dataBaseHelper;
    final String emailOspite = "admin@gmail.com";

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
                if (srcActivity.checkSession()) {
                    srcActivity.nextSessionActivity();

                } else {
                    srcActivity.nextActivity();
                }

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
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
        //checkSession();

    }


    private void nextActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void nextSessionActivity() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }


    private boolean checkSession() {
        boolean flag = false;
        SessionManagement sessionManagement = new SessionManagement(SplashActivity.this);
        String emailCuratore = sessionManagement.getSession();
        if (emailCuratore.compareTo("-1") != 0) {
            if (emailCuratore.compareTo(emailOspite) == 0) {
                sessionManagement.removeSession();
                dataBaseHelper.setEmailCuratore(emailOspite);
                flag = false;
            } else {
                flag = true;
                dataBaseHelper.setEmailCuratore(sessionManagement.getSession());
            }
        }


        return flag;
    }
}