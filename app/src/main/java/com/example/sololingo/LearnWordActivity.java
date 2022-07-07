package com.example.sololingo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Bean.Subject;
import Bean.Word;
import DAO.SubjectDAO;

public class LearnWordActivity extends AppCompatActivity {
    private SubjectDAO subjectDAO;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Word> wordList = new ArrayList<>();
    private Intent receiveIntent;
    private Subject subject;
    private View vLeft, vRight;
    private TextView tvWord, tvWordDef;

    public void bindingView(){
        vLeft = findViewById(R.id.vLeft);
        vRight = findViewById(R.id.vRight);
        tvWord = findViewById(R.id.tvWord);
        tvWordDef = findViewById(R.id.tvWordDef);

        receiveIntent = getIntent();
        subject = (Subject) receiveIntent.getSerializableExtra("subject");
    }

    public void bindingAction(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
    }
}