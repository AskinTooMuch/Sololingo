package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import Bean.Memo;
import Bean.User;

public class ActivityMemoDetail extends AppCompatActivity {
    private ImageButton ibtnMemoReturn;
    private ImageButton ibtnMemoCancel;
    private EditText edtMemoTitle;
    private EditText edtMemoContent;
    private boolean saveFlag = true;
    private FirebaseFirestore db;
    private SharedPreferences sharedPref;
    private FirebaseAuth firebaseAuth;

    private void bindingView(){
        ibtnMemoReturn = findViewById(R.id.ibtnMemoReturn);
        ibtnMemoCancel = findViewById(R.id.ibtnMemoCancel);
        edtMemoTitle = findViewById(R.id.edtMemoTitle);
        edtMemoContent = findViewById(R.id.edtMemoContent);

    }

    private void bindingAction(){
        ibtnMemoReturn.setOnClickListener(this::finishActivity);
        ibtnMemoCancel.setOnClickListener(this::finishActivityNoSave);
    }

    /*private void loadData(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }*/

    private void finishActivityNoSave(View view) {
        saveFlag = false;
        addMemo();
        finish();
    }

    private void finishActivity(View view) {
        finish();
    }

    private void intentInitial(){
        Intent comingIntent = getIntent();
        if (comingIntent!=null){
            String action = comingIntent.getStringExtra("action");
            switch (action){
                case "blankMemo":
                    //Do nothing since it's blank, wow
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        bindingView();
        bindingAction();
        db = FirebaseFirestore.getInstance();
        sharedPref = getApplicationContext().getSharedPreferences("LoginInformation",this.MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (saveFlag){
            addMemo();
            Log.d("Add data", "onDestroy: doin stuff");
        }
    }

    private void addMemo() {
        //Get memo detail
        String title = edtMemoTitle.getText().toString();
        String content = edtMemoContent.getText().toString();
        String userEmail = sharedPref.getString("email","");
        Date lastModified = new Date();
        Memo newMemo = new Memo(userEmail, lastModified, lastModified, title, content);
        // Add a new document with a generated ID
        Log.d("Add data", "addMemo: Adding");
        firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser curUser = firebaseAuth.getCurrentUser();
        db.collection("memo")
                .add(newMemo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Add data", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Add data", "Error adding document", e);
                    }
                });
    }
}