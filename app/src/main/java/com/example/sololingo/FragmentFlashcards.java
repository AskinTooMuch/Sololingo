package com.example.sololingo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.SubjectListAdapter;
import Bean.Subject;
import DAO.SubjectDAO;

public class FragmentFlashcards extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<Subject> subjectList = new ArrayList<>();
    private ArrayList<Subject> subjectListFull = new ArrayList<>();
    private RecyclerView rcvItems;
    private SubjectListAdapter mSubjectListAdapter;
    private View vAddSubject;
    private SubjectDAO subjectDAO = new SubjectDAO();
    ;

    private void bindingView(View view) {
        rcvItems = view.findViewById(R.id.rcvList);
        vAddSubject = view.findViewById(R.id.vAddSubject);
        database = FirebaseDatabase.getInstance();
    }

    private void bindingAction(View view) {
        vAddSubject.setOnClickListener(this::addSubject);
    }

    public int getNextId() {
        /*
        ArrayList<Integer> listId = new ArrayList<>();
        for (Subject s : subjectList) {
            listId.add(s.getId());
        }
        Collections.sort(listId);
        if (listId.get(0)!=1) return 1;
        for (int i = 0; i < listId.size()-1; i++){
            if ((listId.get(i)+1)!= listId.get(i+1)) return i+2;
        }
        return listId.size()+1;

         */
        return subjectListFull.get(subjectListFull.size() - 1).getId() + 1;
    }

    private void addSubject(View view) {
        Intent intent = new Intent(view.getContext(), AddSubjectActivity.class);
        intent.putExtra("nextId", getNextId());
        view.getContext().startActivity(intent);
    }

    private void getSubjectList() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getEmail();
        myRef = database.getReference("subject");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Subject subject = (Subject) dataSnapshot.getValue(Subject.class);
                    subjectListFull.add(subject);
                    if (subject.getuId().equalsIgnoreCase(uId)) {
                        subjectList.add(subject);
                    }
                }
                mSubjectListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inflateSubjectList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvItems.setLayoutManager(linearLayoutManager);

        mSubjectListAdapter = new SubjectListAdapter(getActivity(), subjectList);
        rcvItems.setAdapter(mSubjectListAdapter);
        subjectList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_flashcards, container, false);
        bindingView(mView);
        bindingAction(mView);
        inflateSubjectList();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSubjectList();
        inflateSubjectList();
    }
}