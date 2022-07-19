package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private SubjectDAO subjectDAO= new SubjectDAO();

    private void bindingView() {
        edtSubjectName = findViewById(R.id.edtSubjectName);
        vCheck = findViewById(R.id.vCheck);
        database = FirebaseDatabase.getInstance();
    }

    private void bindingAction() {
        vCheck.setOnClickListener(this::addSubject);
    }

    private Subject getSubject() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("nextId", 0);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getEmail();
        String name = edtSubjectName.getText().toString().trim();
        if (name.equalsIgnoreCase("")) name = "Unnamed subject";
        return new Subject(id, uId, name, 0);
    }

    private void addSubject(View view) {
        Subject s = getSubject();
        subjectDAO.addSubject(s);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        bindingView();
        bindingAction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_Home:
                Intent intent = new Intent(AddSubjectActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}