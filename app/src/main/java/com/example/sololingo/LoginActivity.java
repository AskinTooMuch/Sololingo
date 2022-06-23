package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edEmail,edPassword;
    Button btnLogin,btnRedirectRegister;
    CheckBox cbRememberMe;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindingView();
        bindingAction();
    }


    private void bindingView() {
        edEmail = findViewById(R.id.edEmailLogin);
        edPassword = findViewById(R.id.edPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRedirectRegister = findViewById(R.id.btnRedirectRegister);
        cbRememberMe = findViewById(R.id.cbRememberMe);
    }

    private void bindingAction() {
        btnLogin.setOnClickListener(this::onLogin);
        btnRedirectRegister.setOnClickListener(this::redirectRegister);
    }

    private void redirectRegister(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void rememberMe(String email, String password){
        if(cbRememberMe.isChecked()){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginInformation",this.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("email",email);
            editor.putString("password",password);
            editor.commit();
        }
    }

    private void onLogin(View view) {
        String email  = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        rememberMe(email,password);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                        //chuyen ve home
                    }else{
                        Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}