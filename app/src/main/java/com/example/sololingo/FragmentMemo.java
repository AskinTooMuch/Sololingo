package com.example.sololingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMemo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMemo extends Fragment {
    private FirebaseDatabase database;
    private ImageButton ibtnNewMemo;

    private void bindingView(View view){
       database = FirebaseDatabase.getInstance();
       ibtnNewMemo = view.findViewById(R.id.ibtnNewMemo);
    }

    private void bindingAction(View view){
        ibtnNewMemo.setOnClickListener(this::onNewMemoClick);

    }

    /**
     * Create a new blank memo
     * @param view
     */
    private void onNewMemoClick(View view) {
        //Todo: Add activity memo detail
        Intent i = new Intent(view.getContext(), ActivityMemoDetail.class);
        i.putExtra("action","blankMemo");
        startActivity(i);
    }

    private void onEditMemoClick(View view) {
        //Todo: Add callback to memo detail
    }

    public FragmentMemo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentMemo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMemo newInstance(String param1, String param2) {
        FragmentMemo fragment = new FragmentMemo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView(view);
        bindingAction(view);
    }
}