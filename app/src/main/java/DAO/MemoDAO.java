package DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import Bean.Memo;
import Bean.User;

public class MemoDAO {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPref;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "MemoDAO";

    public FirestoreRecyclerOptions<Memo> getMemoSyncOptions(Context context){
        sharedPref = context.getSharedPreferences("LoginInformation", context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        String userMail = sharedPref.getString("email", "");
        Query query = FirebaseFirestore
                .getInstance()
                .collection("memo")
                .whereEqualTo("userEmail", userMail)
                ;
        FirestoreRecyclerOptions<Memo> options = new FirestoreRecyclerOptions.Builder<Memo>()
                .setQuery(query, Memo.class)
                .build();
        return options;
    }

    public void deleteMemo(String id){
        db.collection("memo").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error deleting document "+e);
                    }
                });
        return;
    }

    public void getMemo(String id, FirebaseCallBack firebaseCallBack){
        firebaseAuth = FirebaseAuth.getInstance();
        db.collection("memo")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                firebaseCallBack.onCallBack(document.toObject(Memo.class));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        return;
    }

    public void addMemo(Memo memo){
        // Add a new document with a generated ID
        //FirebaseUser curUser = firebaseAuth.getCurrentUser();
        db.collection("memo")
                .add(memo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document", e);
                    }
                });
        return;
    }

    public void updateMemo(Memo memo){
        firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser curUser = firebaseAuth.getCurrentUser();
        db.collection("memo").document(memo.getId())
                .set(memo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public interface FirebaseCallBack {
        void onCallBack(ArrayList<Memo> memoList);

        void onCallBack(Memo memo);

    }
}
