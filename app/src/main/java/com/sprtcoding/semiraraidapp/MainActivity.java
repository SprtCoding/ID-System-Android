package com.sprtcoding.semiraraidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {
    private FirebaseAuth _mAuth;
    private ImageView _logoutBtn;
    private ProgressDialog _loadingBar, _loadingBar2;
    private MaterialButton _getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _auth();
        _var();
        _loading();

        _logoutBtn.setOnClickListener(view -> {
            _loadingBar.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                _mAuth.signOut();
                _loadingBar.dismiss();
                Intent i = new Intent(MainActivity.this, LoginPage.class);
                startActivity(i);
                finish();
            }, 3000);
        });

        _getStartedBtn.setOnClickListener(view -> {
            _loadingBar2.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                _mAuth.signOut();
                _loadingBar2.dismiss();
                Intent i = new Intent(MainActivity.this, Form.class);
                startActivity(i);
                finish();
            }, 3000);
        });
    }

    private void _loading() {
        _loadingBar = new ProgressDialog(MainActivity.this);
        _loadingBar.setTitle("Logout");
        _loadingBar.setMessage("Please wait...");
        _loadingBar.setCanceledOnTouchOutside(true);

        _loadingBar2 = new ProgressDialog(MainActivity.this);
        _loadingBar2.setTitle("Loading");
        _loadingBar2.setMessage("Please wait...");
        _loadingBar2.setCanceledOnTouchOutside(true);
    }

    private void _var() {
        _logoutBtn = findViewById(R.id.logoutBtn);
        _getStartedBtn = findViewById(R.id.getStartedBtn);
    }

    private void _auth() {
        _mAuth = FirebaseAuth.getInstance();
//        FirebaseUser _mUSer = _mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}