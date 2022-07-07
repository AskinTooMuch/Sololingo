package com.example.sololingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import Bean.Memo;
import Bean.User;
import DAO.MemoDAO;
import DAO.UserDAO;

public class ActivityMemoDetail extends AppCompatActivity {
    public static final String ACTION_DETAIL = "memoDetail";
    public static final String TAG = "++++++++++++++++++";
    private ImageButton ibtnMemoReturn;
    private ImageButton ibtnMemoCancel;
    private EditText edtMemoTitle;
    private EditText edtMemoContent;
    private boolean saveFlag = true;
    private Memo memo;
    private String action;
    private String id;
    private SharedPreferences sharedPref;
    private MemoDAO memoDAO;

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

    private void finishActivityNoSave(View view) {
        Log.d(TAG, "finishActivityNoSave: Delete");
        saveFlag = false;
        if (Objects.equals(action, ACTION_DETAIL)){
            memoDAO.deleteMemo(id);
        }
        finish();
    }

    private void finishActivity(View view) {
        finish();
    }

    private void intentInitial(){
        Intent comingIntent = getIntent();
        if (comingIntent!=null){
            action = comingIntent.getStringExtra("action");
            switch (action){
                case "blankMemo":
                    //Do nothing since it's blank, wow
                    break;
                case ACTION_DETAIL:
                    //Get memo detail
                    id = comingIntent.getStringExtra("id");
                    Log.d(TAG, "intentInitial: "+id);
                    memoDAO.getMemo(id, new MemoDAO.FirebaseCallBack() {
                        @Override
                        public void onCallBack(ArrayList<Memo> memoList) {

                        }

                        @Override
                        public void onCallBack(Memo memo) {
                            try {
                                edtMemoTitle.setText(memo.getTitle());
                                edtMemoContent.setText(memo.getContent());
                            } catch (NullPointerException n){
                                Log.d(TAG, "intentInitial: null content");
                                finish();
                            }
                        }
                    });
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
        memoDAO = new MemoDAO();
        sharedPref = getApplicationContext().getSharedPreferences("LoginInformation",this.MODE_PRIVATE);
        intentInitial();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((memo!=null) &&
                (edtMemoTitle.getText().toString().equals(memo.getTitle())
                && edtMemoContent.getText().toString().equals(memo.getContent()))) {
            saveFlag=false;
        }
        Log.d(TAG, "onDestroy: "+saveFlag);
        if (saveFlag){
            if (action.equals(ACTION_DETAIL)){
                Log.d(TAG, "onDestroy: UPDATE");
                updateMemo();
            } else {
                addMemo();
            }
        }
    }

    private void updateMemo() {
        Log.d(TAG, "updateMemo: update");
        //Get memo detail
        String title = edtMemoTitle.getText().toString();
        String content = edtMemoContent.getText().toString();
        if (title.isEmpty() && content.isEmpty()){
            return;
        }
        String userEmail = sharedPref.getString("email","");
        Date lastModified = new Date();
        Memo updateMemo = new Memo(userEmail, lastModified, lastModified, title, content, id);
        memoDAO.updateMemo(updateMemo);
    }

    private void addMemo() {
        Log.d(TAG, "addMemo: add");
        //Get memo detail
        String title = edtMemoTitle.getText().toString();
        String content = edtMemoContent.getText().toString();
        if (title.isEmpty() && content.isEmpty()){
            return;
        }
        String userEmail = sharedPref.getString("email","");
        Date lastModified = new Date();
        Memo newMemo = new Memo(userEmail, lastModified, lastModified, title, content, null);
        memoDAO.addMemo(newMemo);
    }
}