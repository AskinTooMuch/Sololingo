package com.example.sololingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.SubjectListAdapter;
import Adapter.WordListAdapter;
import Bean.Subject;
import Bean.Word;

public class FragmentFlashcards extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<Subject> subjectList = new ArrayList<>();
    private RecyclerView rcvItems;
    private SubjectListAdapter mSubjectListAdapter;
    private View vAddSubject;

    private void bindingView(View view) {
        rcvItems = view.findViewById(R.id.rcvList);
        vAddSubject = view.findViewById(R.id.vAddSubject);
        database = FirebaseDatabase.getInstance();
    }

    private void bindingAction(View view) {
        getSubjectList();
        inflateSubjectList();
        vAddSubject.setOnClickListener(this::addSubject);
    }

    private void addSubject(View view) {
        Intent intent = new Intent(view.getContext(),AddSubjectActivity.class);
        view.getContext().startActivity(intent);

    }

    private void getSubjectList() {
        String uId = "vvduong108@gmail.com";
        myRef = database.getReference("subject");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Subject subject = dataSnapshot.getValue(Subject.class);
                    if (subject.getuId().equalsIgnoreCase(uId)){
                        subjectList.add(subject);
                    }
                }
                mSubjectListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inflateSubjectList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvItems.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL);
        rcvItems.addItemDecoration(dividerItemDecoration);
        mSubjectListAdapter = new SubjectListAdapter(getActivity(),subjectList);
        rcvItems.setAdapter(mSubjectListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_flashcards, container, false);
        bindingView(mView);
        bindingAction(mView);




        return mView;
    }

}