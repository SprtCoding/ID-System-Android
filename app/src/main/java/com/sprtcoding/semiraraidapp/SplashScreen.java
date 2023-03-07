package com.sprtcoding.semiraraidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends BaseActivity {
    private FirebaseUser _mUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        _auth();
    }

    private void _auth() {
        FirebaseAuth _mAuth = FirebaseAuth.getInstance();
        _mUSer = _mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        Handler handler = new Handler();
        if(_mUSer != null) {
            handler.postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }, 3000);
        }else {
            handler.postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(i);
                finish();
            }, 3000);
        }

    }
}