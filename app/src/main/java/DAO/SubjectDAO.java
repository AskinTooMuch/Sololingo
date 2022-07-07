package DAO;

import android.content.Context;
import android.content.SharedPreferences;
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

import Bean.Subject;
import Bean.User;

public class SubjectDAO {
    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;

    public void addSubject(Subject subject){
        myRef = database.getReference("subject");
        myRef.child(String.valueOf(subject.getId())).setValue(subject);
    }

    public void getSubjectList(FirebaseCallBack firebaseCallBack){
        /*
        SharedPreferences pref = context.getSharedPreferences("LoginInformation",context.MODE_PRIVATE);
        String email = pref.getString("email","");
        */
        String uId = "vvduong108@gmail.com";
        ArrayList<Subject> subjectList = new ArrayList<>();
        ArrayList<Subject> subjectListFull = new ArrayList<>();
        myRef = database.getReference("subject");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Subject subject = (Subject) dataSnapshot.getValue(Subject.class);
                    subjectListFull.add(subject);
                    if (subject.getuId().equalsIgnoreCase(uId)) {
                        subjectList.add(subject);
                    }
                }
                firebaseCallBack.onCallBack(subjectList, subjectListFull);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        /*myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Subject subject = (Subject) snapshot.getValue(Subject.class);
                        subjectListFull.add(subject);
                        if (subject.getuId().equalsIgnoreCase(uId)) {
                            subjectList.add(subject);
                        }
                    }
                    firebaseCallBack.onCallBack(subjectList, subjectListFull);
                }
            }
        });*/

    }
    public interface FirebaseCallBack {
        void onCallBack(ArrayList<Subject> sList, ArrayList<Subject> sListFull);
    }
}
