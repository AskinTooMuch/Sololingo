package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.SubjectListAdapter;
import Adapter.WordListAdapter;
import Bean.Subject;
import Bean.Word;

public class ViewWordsActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Word> wordList = new ArrayList<>();

    private ArrayList<Word> wordListFull = new ArrayList<>();
    private WordListAdapter mWordListAdapter;
    private RecyclerView rcvItems;
    private TextView tvSubject;
    private Intent receiveIntent;
    private Subject subject;
    private View vAddWord, vEdit, vDelete, vLearn;

    private void bindingView() {
        tvSubject = findViewById(R.id.tvSubject);
        rcvItems = findViewById(R.id.rcvList);
        vAddWord = findViewById(R.id.vAddWord);
        vEdit = findViewById(R.id.vEdit);
        vDelete = findViewById(R.id.vDelete);
        vLearn = findViewById(R.id.vLearn);
        database = FirebaseDatabase.getInstance();

        receiveIntent = getIntent();
        subject = (Subject) receiveIntent.getSerializableExtra("subject");
        tvSubject.setText(subject.getName());
    }

    private void bindingAction() {
        vAddWord.setOnClickListener(this::addWord);
        vEdit.setOnClickListener(this::editSubject);
        vDelete.setOnClickListener(this::deleteSubject);
        vLearn.setOnClickListener(this::learn);
    }

    private void learn(View view) {
        Intent intent = new Intent(ViewWordsActivity.this, LearnActivity.class);
        intent.putExtra("subject",subject);
        startActivity(intent);
    }

    private void editSubject(View view) {
        Intent intent = new Intent(ViewWordsActivity.this, EditSubjectActivity.class);
        intent.putExtra("subject",subject);
        startActivity(intent);
    }

    private void deleteSubject(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(getDrawable(R.drawable.sologo))
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.msg_confirmDeleteSubject))
                .setCancelable(true)
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myRef = database.getReference("subject/" + subject.getId());
                                myRef.removeValue();
                                for (Word word : wordList){
                                    myRef = database.getReference("word/" + word.getId());
                                    myRef.removeValue();
                                    Toast.makeText(ViewWordsActivity.this, getString(R.string.msg_deleteSubjectSuccess), Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }
                        })

                .setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void addWord(View view) {
        Intent intent = new Intent(this, AddWordActivity.class);
        intent.putExtra("subject", subject);
        intent.putExtra("nextId", getNextId());
        this.startActivity(intent);
    }

    public int getNextId() {
        return wordListFull.get(wordListFull.size() - 1).getId() + 1;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getWordsList() {
        wordList.clear();
        myRef = database.getReference("word");
        int subjectId = subject.getId();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Word word = dataSnapshot.getValue(Word.class);
                    wordListFull.add(word);
                    if (word.getSubjectId() == subjectId) {
                        wordList.add(word);
                    }
                }
                mWordListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewWordsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void inflateWordsList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewWordsActivity.this);
        rcvItems.setLayoutManager(linearLayoutManager);

        mWordListAdapter = new WordListAdapter(ViewWordsActivity.this, wordList);
        rcvItems.setAdapter(mWordListAdapter);
        wordList.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_words);
        bindingView();
        bindingAction();
        //getWordsList();
        inflateWordsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        getWordsList();
        inflateWordsList();
    }

}