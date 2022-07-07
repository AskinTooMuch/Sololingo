package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Bean.Subject;
import DAO.SubjectDAO;

public class EditSubjectActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;
    private EditText edtSubjectName;
    private View vCheck;
    private SubjectDAO subjectDAO;
    private Intent receiveIntent;
    private Subject subject;

    private void bindingView() {
        subjectDAO = new SubjectDAO();
        receiveIntent = getIntent();
        edtSubjectName = findViewById(R.id.edtSubjectName);
        vCheck = findViewById(R.id.vCheck);
        database = FirebaseDatabase.getInstance();
        subject = (Subject) receiveIntent.getSerializableExtra("subject");
        edtSubjectName.setText(subject.getName());
    }

    private void bindingAction() {
        vCheck.setOnClickListener(this::editSubject);
    }

    private void editSubject(View view) {
        int id = subject.getId();
        String newName = edtSubjectName.getText().toString();

        myRef = database.getReference("subject/" + id + "/name");
        myRef.setValue(newName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(EditSubjectActivity.this, getString(R.string.msg_updateSubject), Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(EditSubjectActivity.this, ViewWordsActivity.class);
        subject.setName(newName);
        intent.putExtra("subject", subject);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        bindingView();
        bindingAction();
    }
}