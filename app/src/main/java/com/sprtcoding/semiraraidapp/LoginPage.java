package com.sprtcoding.semiraraidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginPage extends BaseActivity {
    private MaterialButton _loginBtn;
    private TextInputEditText _email, _password;
    private ProgressDialog _loadingBar;
    private FirebaseAuth _mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        _myVar();
        _auth();
        _loading();

        _loginBtn.setOnClickListener(view -> {
            _loadingBar.show();
            String __email = Objects.requireNonNull(_email.getText()).toString().trim();
            String __password = Objects.requireNonNull(_password.getText()).toString().trim();
            if(TextUtils.isEmpty(__email)) {
                Toast.makeText(this, "Email is empty.", Toast.LENGTH_SHORT).show();
                _loadingBar.dismiss();
            }else if(TextUtils.isEmpty(__password)) {
                Toast.makeText(this, "Password is empty.", Toast.LENGTH_SHORT).show();
                _loadingBar.dismiss();
            }else {
                _mAuth.signInWithEmailAndPassword(__email, __password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Intent _intent = new Intent(this, MainActivity.class);
                        startActivity(_intent);
                        finish();
                        _loadingBar.dismiss();
                    }else {
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        _loadingBar.dismiss();
                    }
                });
            }
        });

    }

    private void _auth() {
        _mAuth = FirebaseAuth.getInstance();
    }

    private void _loading() {
        _loadingBar = new ProgressDialog(LoginPage.this);
        _loadingBar.setTitle("Login");
        _loadingBar.setMessage("Please wait...");
        _loadingBar.setCanceledOnTouchOutside(true);
    }
    private void _myVar() {
        _loginBtn = findViewById(R.id.loginBtn);
        _email = findViewById(R.id.emailET);
        _password = findViewById(R.id.passET);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}