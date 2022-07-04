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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Bean.User;
import DAO.UserDAO;

public class UserProfile extends AppCompatActivity {

    private EditText edName, edDOB, edOldPassword, edNewPassword;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private TextView tvEmail;
    private Button btnUpdate, btnLogout;
    private FirebaseAuth firebaseAuth;
    private String name;
    private String DOB;
    private String password;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        bindingView();
        bindingAction();
    }

    private void bindingView() {
        tvEmail = findViewById(R.id.tvUserEmail);
        edName = findViewById(R.id.edUpdateName);
        edDOB = findViewById(R.id.edUpdateDOB);
        edOldPassword = findViewById(R.id.edUpdateOldPassword);
        edNewPassword = findViewById(R.id.edUpdateNewPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void bindingAction() {
        addUserProfileContent();
        edDOB.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar calendar = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");
                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        calendar.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        calendar.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > calendar.getActualMaximum(Calendar.DATE)) ? calendar.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
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
        btnUpdate.setOnClickListener(this::onBtnUpdateClick);
        btnLogout.setOnClickListener(this::onBtnLogoutClick);
    }

    private void onBtnLogoutClick(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void addUserProfileContent() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        UserDAO userDAO = new UserDAO();
        userDAO.getUserByEmail(currentUser.getEmail(), new UserDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<User> userList) {

            }

            @Override
            public void onCallBack(User user) {
                tvEmail.setText(user.getEmail());
                edName.setText(user.getName());
                edDOB.setText(user.getDob());
            }
        });
    }

    private void onBtnUpdateClick(View view) {
        name = edName.getText().toString().trim();
        DOB = edDOB.getText().toString().trim();
        password = edOldPassword.getText().toString().trim();
        newPassword = edNewPassword.getText().toString().trim();
        Date date = null;
        try {
            date = SIMPLE_DATE_FORMAT.parse(DOB);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (name.length() == 0 || name == null ||
                DOB.length() == 0 || DOB == null) {
            Toast.makeText(this, "You have to input all form", Toast.LENGTH_SHORT).show();
        } else if (date == null || date.after(new java.util.Date())) {
            Toast.makeText(this, "Your date of birth is wrong!", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User(tvEmail.getText().toString().trim(), name, DOB);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = db.getReference("user");
            dbRef.child(user.getUserPath()).setValue(user);
        }
        if ((password.length() == 0 || password == null) &&
                (newPassword.length() != 0 && newPassword != null)) {
            Toast.makeText(getApplicationContext(), "Your have to input old password", Toast.LENGTH_SHORT).show();
        } else if ((password.length() != 0 && password != null) &&
                (newPassword.length() == 0 || newPassword == null)) {
            Toast.makeText(getApplicationContext(), "Your have to input new password", Toast.LENGTH_SHORT).show();
        } else if (password.length() != 0 && password != null &&
                newPassword.length() != 0 && newPassword != null) {
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();


            AuthCredential credential = EmailAuthProvider
                    .getCredential(currentUser.getEmail(), password);
            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Your password have been changed", Toast.LENGTH_SHORT).show();
                                    edOldPassword.setText("");
                                    edNewPassword.setText("");
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Change password fail" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Your old password is wrong", Toast.LENGTH_SHORT).show();
                        edOldPassword.setText("");
                        edNewPassword.setText("");
                    }
                }
            });
        }
    }

}