package DAO;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Bean.User;

public class UserDAO {
    public void getUserByEmail(String email, FirebaseCallBack firebaseCallBack){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("user");
        dbRef.child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    User user2 = task.getResult().getValue(User.class);
                    firebaseCallBack.onCallBack(user2);
                }

            }
        });
    }

    public void addNewUser(User user){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("user");
    }

    public interface FirebaseCallBack {
        void onCallBack(ArrayList<User> userList);
        void onCallBack(User user);

    }
}
