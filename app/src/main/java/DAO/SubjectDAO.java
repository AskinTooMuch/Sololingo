package DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Bean.Subject;
import Bean.User;

public class SubjectDAO {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;

    public void addSubject(Subject subject) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("subject");
        myRef.child(String.valueOf(subject.getId())).setValue(subject);
    }

    public void getSubjectList(SubjectDAO.FirebaseCallBack firebaseCallBack) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getEmail();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("subject");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    ArrayList<Subject> subjectList = new ArrayList<>();
                    ArrayList<Subject> subjectListFull = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Subject subject = (Subject) snapshot.getValue(Subject.class);
                        subjectListFull.add(subject);
                        if (subject.getuId().equalsIgnoreCase(uId)) {
                            subjectList.add(subject);
                        }
                    }
                    firebaseCallBack.onCallBack(subjectList, subjectListFull);
                }
            }
        });
    }

    public void updateSubjectName(int id, String newName) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("subject/" + id + "/name");
        myRef.setValue(newName);
    }

    public void deleteSubject(int id) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("subject/" + id);
        myRef.removeValue();
    }

    public interface FirebaseCallBack {
        void onCallBack(ArrayList<Subject> sList, ArrayList<Subject> sListFull);
    }
}
