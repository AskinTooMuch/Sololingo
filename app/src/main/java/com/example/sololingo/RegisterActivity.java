package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import Bean.User;

public class RegisterActivity extends AppCompatActivity {

    EditText edEmail,edPassword,edName,edDOB,edRePassword;
    Button btnRegister,btnLogin;
    FirebaseAuth firebaseAuth;
    String email;
    String name;
    String DOB;
    String password;
    String rePassword;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindingView();
        bindingAction();
    }

    private void bindingView() {
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edName = findViewById(R.id.edName);
        edDOB = findViewById(R.id.edDOB);
        edRePassword = findViewById(R.id.edRePassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnRedirectLogin);
    }

    private void bindingAction() {
        edDOB.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar calendar = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");
                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        calendar.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        calendar.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > calendar.getActualMaximum(Calendar.DATE))? calendar.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edDOB.setText(current);
                    edDOB.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnRegister.setOnClickListener(this::onRegister);
        btnLogin.setOnClickListener(this::onLogin);
    }

    private void onLogin(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void onRegister(View view) {
        email = edEmail.getText().toString().trim();
        name = edName.getText().toString().trim();
        DOB = edDOB.getText().toString().trim();
        password = edPassword.getText().toString().trim();
        rePassword = edRePassword.getText().toString().trim();
        Date date = null;
        try {
            date = SIMPLE_DATE_FORMAT.parse(DOB);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(email.length() == 0 || email == null ||
                name.length() == 0 || name == null ||
                DOB.length() == 0 || DOB == null ||
                password.length() == 0 || password == null ||
                rePassword.length() == 0 || rePassword == null){
            Toast.makeText(this,"You have to input all form",Toast.LENGTH_SHORT).show();
        }else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
            Toast.makeText(this,"Your email is wrong!",Toast.LENGTH_SHORT).show();
        }else if (!password.equals(rePassword)){
            Toast.makeText(this,"Your password not matching with re-password",Toast.LENGTH_SHORT).show();
        }else if (date == null || date.after( new java.util.Date())){
            Toast.makeText(this,"Your date of birth is wrong!",Toast.LENGTH_SHORT).show();
        }else {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(RegisterActivity.this, "Register successful, check your email to enable your account", Toast.LENGTH_SHORT).show();
                                        saveUserInfor();
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveUserInfor() {
        User user = new User(email,name,DOB);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("user");
        dbRef.child(user.getUserPath()).setValue(user);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


}