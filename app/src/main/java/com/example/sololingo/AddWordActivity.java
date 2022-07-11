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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Bean.Subject;
import Bean.Word;
import DAO.WordDAO;

public class AddWordActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;
    private EditText edtWord, edtWordDef;
    private View vCheck;
    private WordDAO wordDAO;
    private Subject subject;

    public void bidingView() {
        wordDAO = new WordDAO();
        edtWord = findViewById(R.id.edtWord);
        edtWordDef = findViewById(R.id.edtWordDef);
        vCheck = findViewById(R.id.vCheck);
        database = FirebaseDatabase.getInstance();
    }

    public void bindingAction() {
        vCheck.setOnClickListener(this::addWord);
    }

    public Word getWord() {
        Intent intent = getIntent();
        subject = (Subject) intent.getSerializableExtra("subject");
        int id = intent.getIntExtra("nextId", 0);
        String word = edtWord.getText().toString();
        String wordDef = edtWordDef.getText().toString();
        return new Word(id, subject.getId(), word, wordDef);
    }

    public void addWord(View view) {
        Word word = getWord();
        wordDAO.addWord(word);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        bidingView();
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
                Intent intent = new Intent(AddWordActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}