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

    public String convertUserPath(String email) {
        return email.replace('.', '_')
                .replace('#', '_')
                .replace('$', '_')
                .replace('[', '_')
                .replace(']', '_');
    }

    public void getUserByEmail(String email, FirebaseCallBack firebaseCallBack) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("user");
        User user = new User();
        user.setEmail(email);
        dbRef.child(user.getUserPath()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    User user2 = task.getResult().getValue(User.class);
                    firebaseCallBack.onCallBack(user2);
                }
            }
        });

    }

    public void addNewUser(User user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("user");
    }

    public interface FirebaseCallBack {
        void onCallBack(ArrayList<User> userList);

        void onCallBack(User user);

    }
}
