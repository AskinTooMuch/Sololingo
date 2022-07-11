package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Bean.Subject;
import Bean.Word;
import DAO.SubjectDAO;

public class LearnActivity extends AppCompatActivity {
    private SubjectDAO subjectDAO;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Word> wordList = new ArrayList<>();
    private Intent receiveIntent;
    private Subject subject;
    private View vLeft, vRight;
    private TextView tvWord, tvWordDef, tvProgress;

    public void bindingView() {
        vLeft = findViewById(R.id.vLeft);
        vRight = findViewById(R.id.vRight);
        tvWord = findViewById(R.id.tvWord);
        tvWordDef = findViewById(R.id.tvWordDef);
        tvProgress = findViewById(R.id.tvProgress);
        database = FirebaseDatabase.getInstance();
        receiveIntent = getIntent();
        subject = (Subject) receiveIntent.getSerializableExtra("subject");
    }

    public void bindingAction() {
        getListWord();
        vLeft.setOnClickListener(this::moveLeft);
        vRight.setOnClickListener(this::moveRight);
    }

    public void setProgress() {
        tvProgress.setText(subject.getCurProgress() + 1 + "/" + wordList.size());
    }

    private void moveRight(View view) {
        if (subject.getCurProgress() < wordList.size() - 1) {
            String progress = (subject.getCurProgress() + 2) + "/" + wordList.size();
            tvProgress.setText(progress);

            tvWord.setText(wordList.get(subject.getCurProgress() + 1).getWord());
            tvWordDef.setText(wordList.get(subject.getCurProgress() + 1).getDefinition());
            subject.setCurProgress(subject.getCurProgress() + 1);
            myRef = database.getReference("subject/" + subject.getId());
            myRef.setValue(subject);
        }
    }

    private void moveLeft(View view) {
        if (subject.getCurProgress() != 0) {
            String progress = (subject.getCurProgress()) + "/" + wordList.size();
            tvProgress.setText(progress);

            tvWord.setText(wordList.get(subject.getCurProgress() - 1).getWord());
            tvWordDef.setText(wordList.get(subject.getCurProgress() - 1).getDefinition());
            subject.setCurProgress(subject.getCurProgress() - 1);
            myRef = database.getReference("subject/" + subject.getId());
            myRef.setValue(subject);
        }
    }

    public void getListWord() {
        myRef = database.getReference("word");
        int subjectId = subject.getId();
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Word word = (Word) snapshot.getValue(Word.class);
                        if (word.getSubjectId() == subjectId) {
                            wordList.add(word);
                        }
                    }
                    tvProgress.setText(subject.getCurProgress() + 1 + "/" + wordList.size());

                    tvWord.setText(wordList.get(subject.getCurProgress()).getWord());
                    tvWordDef.setText(wordList.get(subject.getCurProgress()).getDefinition());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
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
                Intent intent = new Intent(LearnActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
