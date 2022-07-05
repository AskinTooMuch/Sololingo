package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private WordListAdapter mWordListAdapter;
    private RecyclerView rcvItems;
    private TextView tvSubject;
    private Intent receiveItent;
    private Subject subject;
    private View vAddWord;

    private void bindingView() {
        tvSubject = findViewById(R.id.tvSubject);
        rcvItems = findViewById(R.id.rcvList);
        vAddWord = findViewById(R.id.vAddWord);
        database = FirebaseDatabase.getInstance();
        receiveItent = getIntent();
        subject = (Subject) receiveItent.getExtras().getSerializable("subject");
    }

    private void bindingAction() {
        tvSubject.setText(subject.getName());
        vAddWord.setOnClickListener(this::addWord);
        getWordsList();
        inflateWordsList();
    }

    private void addWord(View view) {
        Intent intent = new Intent(this,AddWordActivity.class);
        intent.putExtra("subject",subject);
        this.startActivity(intent);
    }

    private void getWordsList() {
        myRef = database.getReference("word");
        int subjectId = subject.getId();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Word word = dataSnapshot.getValue(Word.class);
                    if (word.getSubjectId() == subjectId){
                        wordList.add(word);
                    }
                }
                mWordListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewWordsActivity.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inflateWordsList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewWordsActivity.this);
        rcvItems.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ViewWordsActivity.this,DividerItemDecoration.HORIZONTAL);
        rcvItems.addItemDecoration(dividerItemDecoration);
        mWordListAdapter = new WordListAdapter(ViewWordsActivity.this,wordList);
        rcvItems.setAdapter(mWordListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_words);
        bindingView();
        bindingAction();
    }


}