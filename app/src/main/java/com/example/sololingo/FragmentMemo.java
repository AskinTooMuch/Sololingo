package com.example.sololingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Adapter.MemoRecycleAdapter;
import Bean.Memo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMemo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMemo extends Fragment implements MemoRecycleAdapter.OnMemoClickListener {
    private ImageButton ibtnNewMemo;
    private FirebaseFirestore db;
    private SharedPreferences sharedPref;
    private FirebaseAuth firebaseAuth;
    private MemoRecycleAdapter memoRecycleAdapter;
    private RecyclerView rcvMemoList;

    private void bindingView(View view) {
        rcvMemoList = view.findViewById(R.id.rcvMemoList);
        ibtnNewMemo = view.findViewById(R.id.ibtnNewMemo);
    }

    private void bindingAction(View view) {
        ibtnNewMemo.setOnClickListener(this::onNewMemoClick);
    }

    private void loadData() {
        sharedPref = getContext().getSharedPreferences("LoginInformation", getContext().MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        String userMail = sharedPref.getString("email", "");
        rcvMemoList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Query query = FirebaseFirestore
                .getInstance()
                .collection("memo")
                .whereEqualTo("userEmail", userMail)
                ;
        FirestoreRecyclerOptions<Memo> options = new FirestoreRecyclerOptions.Builder<Memo>()
                .setQuery(query, Memo.class)
                .build();
        memoRecycleAdapter = new MemoRecycleAdapter(options, this);
        rcvMemoList.setAdapter(memoRecycleAdapter);
    }

    @Override
    public void onMemoClick(String id) {
        Toast.makeText(getContext(), id+"", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), ActivityMemoDetail.class);
        i.putExtra("action", "memoDetail");
        i.putExtra("id", id);
        startActivity(i);
    }

    /**
     * Create a new blank memo
     *
     * @param view
     */
    private void onNewMemoClick(View view) {
        //Todo: Add activity memo detail
        Intent i = new Intent(view.getContext(), ActivityMemoDetail.class);
        i.putExtra("action", "blankMemo");
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
    public void onStart() {
        super.onStart();
        loadData();
        memoRecycleAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (memoRecycleAdapter != null) {
            memoRecycleAdapter.stopListening();
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
        db = FirebaseFirestore.getInstance();
    }
}