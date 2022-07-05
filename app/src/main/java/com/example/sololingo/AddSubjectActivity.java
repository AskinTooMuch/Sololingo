package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Bean.Subject;

public class AddSubjectActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private EditText edtSubjectName;
    private View vCheck;

    private void bindingView() {
        edtSubjectName = findViewById(R.id.edtSubjectName);
        vCheck = findViewById(R.id.vCheck);
        database = FirebaseDatabase.getInstance();
    }

    private void bindingAction() {
        vCheck.setOnClickListener(this::addSubject);
    }

    private void addSubject(View view) {
        String uId = "vvduong108@gmail.com";
        myRef = database.getReference("subject");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();
                edtSubjectName.setText(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddSubjectActivity.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        bindingView();
        bindingAction();
    }
}