package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Bean.Subject;
import Bean.Word;
import DAO.WordDAO;

public class EditWordActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;

    private EditText edtWord, edtWordDef;
    private View vCheck, vDelete;
    private WordDAO wordDAO;
    private Word word;
    private Intent receiveIntent;

    public void bindingView() {
        wordDAO = new WordDAO();
        edtWord = findViewById(R.id.edtWord);
        edtWordDef = findViewById(R.id.edtWordDef);
        vCheck = findViewById(R.id.vCheck);
        vDelete = findViewById(R.id.vDelete);
        database = FirebaseDatabase.getInstance();
        receiveIntent = getIntent();
        word = (Word) receiveIntent.getSerializableExtra("word");
        edtWord.setText(word.getWord());
        edtWordDef.setText(word.getDefinition());
    }

    public void bindingAction() {
        vDelete.setOnClickListener(this::deleteWord);
        vCheck.setOnClickListener(this::updateWord);
    }

    private void updateWord(View view) {
        String mWord = edtWord.getText().toString();
        String def = edtWordDef.getText().toString();
        int id = word.getId();
        int sId = word.getSubjectId();
        Word newWord = new Word(id, sId, mWord, def, "123");

        wordDAO.updateWord(newWord);
        /*
        myRef = database.getReference("word/" + id);
        myRef.setValue(newWord);*/
        onBackPressed();
    }

    private void deleteWord(View view) {
        wordDAO.deleteWord(word.getId());

        /*myRef = database.getReference("word/" + word.getId());
        myRef.removeValue();*/
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);
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
                Intent intent = new Intent(EditWordActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}