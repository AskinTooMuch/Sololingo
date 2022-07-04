package com.example.sololingo;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        checkLoginState();
    }

    FirebaseAuth firebaseAuth;

    private void checkLoginState(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginInformation",this.MODE_PRIVATE);
            String email = pref.getString("email",null);
            String password = pref.getString("password",null);
            if(email != null && password != null){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                //chuyen ve home
                                redirectHome();
                            }else{
                                redirectLogin();
                            }
                        }else {
                            redirectLogin();
                        }
                    }
                });
            }
        } else {
            //redirect home page
            redirectHome();
        }
    }

    private void redirectLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void redirectHome(){
        Intent intent = new Intent(this,UserProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
