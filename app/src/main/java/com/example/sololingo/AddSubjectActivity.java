package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Bean.Subject;
import DAO.SubjectDAO;

public class AddSubjectActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;
    private EditText edtSubjectName;
    private View vCheck;
    private SubjectDAO subjectDAO;

    private void bindingView() {
        subjectDAO = new SubjectDAO();
        edtSubjectName = findViewById(R.id.edtSubjectName);
        vCheck = findViewById(R.id.vCheck);
        database = FirebaseDatabase.getInstance();
    }

    private void bindingAction() {
        vCheck.setOnClickListener(this::addSubject);
    }
    private Subject getSubject(){
        Intent intent = getIntent();
        int id = intent.getIntExtra("nextId",0);

        /*
        sharedPref = getApplicationContext().getSharedPreferences("LoginInformation",this.MODE_PRIVATE);
        String email = sharedPref.getString("email","");
        */
        String uId = "vvduong108@gmail.com";

        String name = edtSubjectName.getText().toString();
        return new Subject(id,uId,name);
    }
    private void addSubject(View view) {
        Subject s = getSubject();
        subjectDAO.addSubject(s);
        /*
        myRef = database.getReference("subject");
        myRef.child(String.valueOf(s.getId())).setValue(s);
         */
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        bindingView();
        bindingAction();
    }
}