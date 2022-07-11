package DAO;

import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Bean.Subject;
import Bean.Word;

public class WordDAO {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences sharedPref;

    public void addWord(Word word) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("word");
        myRef.child(String.valueOf(word.getId())).setValue(word);
    }

    public void updateWord(Word word) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("word/" + word.getId());
        myRef.setValue(word);
    }

    public void deleteWord(int id) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("word/" + id);
        myRef.removeValue();
    }
}
