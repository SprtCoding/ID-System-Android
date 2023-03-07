package com.sprtcoding.semiraraidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {
    private CountDownTimer sessionTimer;
    private static final String LAST_ACTIVITY_TIME = "last_activity_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startSessionTimer();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        updateLastActivityTime();
        resetSessionTimer();
    }

    private void startSessionTimer() {
        sessionTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Check for user activity and reset the timer if necessary
                long lastActivity = getLastActivityTime();/* Get the timestamp of the user's last activity */;
                long elapsed = System.currentTimeMillis() - lastActivity;
                if (elapsed < 60000) {
                    // If the user has been active in the last minute, reset the timer
                    cancel();
                    start();
                }
            }

            @Override
            public void onFinish() {
                // Log the user out due to inactivity
                FirebaseAuth.getInstance().signOut();
            }
        };

        sessionTimer.start();
    }

    private void updateLastActivityTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(LAST_ACTIVITY_TIME, System.currentTimeMillis());
        editor.apply();
    }

    private long getLastActivityTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getLong(LAST_ACTIVITY_TIME, 0);
    }

    private void resetSessionTimer() {
        sessionTimer.cancel();
        sessionTimer.start();
    }
}
