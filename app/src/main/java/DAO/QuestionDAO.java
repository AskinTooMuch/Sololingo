package DAO;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Bean.Question;
public class QuestionDAO {
    public void getQuestionListByTopic(String topic, QuestionDAO.FirebaseCallBack firebaseCallBack) {


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Question").child(topic);
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Question> listQuestion = new ArrayList<>();
                    DataSnapshot data = task.getResult();
                    for(DataSnapshot snapshot : data.getChildren()){
                        Question question = snapshot.getValue(Question.class);
                        listQuestion.add(question);
                    }
                    firebaseCallBack.onCallBack(listQuestion);
                }else{
                    Log.d("Duong","Fail load");
                }
            }
        });
    }

    public void createAnswerSheet(String answerSheetId,ArrayList<Question> listQuestion){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("AnswerSheet").child(answerSheetId);
        for (Question question: listQuestion){
            dbRef.child(question.getQuestionId()).setValue(question);}
    }

    public void updateAnswerSheet(String answerSheetId,Question question){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("AnswerSheet").child(answerSheetId);
        dbRef.child(question.getQuestionId()).setValue(question);
    }

    public void getAnswerSheet(String answerSheetId,FirebaseCallBack firebaseCallBack){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("AnswerSheet").child(answerSheetId);
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Question> list = new ArrayList<>();
                    DataSnapshot dataSnapshot = task.getResult();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        list.add(data.getValue(Question.class));
                    }
                    firebaseCallBack.onCallBack(list);
                }
            }
        });
    }

    public interface FirebaseCallBack {
        void onCallBack(ArrayList<Question> listQuestion);
    }
}
