package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import Adapter.MockingTestAdapter;
import Bean.Question;
import DAO.QuestionDAO;

public class MockingTestActivity extends AppCompatActivity {

    public static final String TAKE_TEST_STATUS = "takeTest";
    public static final String REVIEW_TEST_STATUS = "reviewTest";
    private TextView tvBack, tvCurrentQuestion, tvTotalQuestion, tvNext, tvSubmit, tvFinishReview;
    private ViewPager viewPager;
    private MockingTestAdapter mockingTestAdapter;
    private ArrayList<Question> listQuestions = new ArrayList<>();
    private String topicCode, status, answerSheetId;

    private void bindingView() {
        tvBack = findViewById(R.id.tvBack);
        tvCurrentQuestion = findViewById(R.id.tvCurrentQuestion);
        tvTotalQuestion = findViewById(R.id.tvTotalQuestion);
        tvNext = findViewById(R.id.tvNext);
        viewPager = findViewById(R.id.mocking_view_pager);
        tvSubmit = findViewById(R.id.tvSubmit);
        tvFinishReview = findViewById(R.id.tvFinishReview);
    }

    private void bindingAction() {
        initIntent();
        fakeDataBase();
        getListQuestion();
        tvBack.setOnClickListener(this::onBackClick);
        tvNext.setOnClickListener(this::onNextClick);
        tvSubmit.setOnClickListener(this::onSubmitClick);
        tvFinishReview.setOnClickListener(this::onFinishReviewClick);
    }

    private void initIntent() {
        Intent intent = getIntent();
        topicCode = intent.getStringExtra("topic_code");
        status = intent.getStringExtra("status");
        answerSheetId = intent.getStringExtra("answer_sheet_id");
    }

    private void getListQuestion() {
        QuestionDAO questionDAO = new QuestionDAO();
        if (status.equalsIgnoreCase(TAKE_TEST_STATUS)) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            answerSheetId = convertToPath(firebaseUser.getEmail()) + "_" + topicCode;
            questionDAO.getQuestionListByTopic(topicCode, new QuestionDAO.FirebaseCallBack() {
                @Override
                public void onCallBack(ArrayList<Question> listQuestion) {
                    listQuestions = listQuestion;
                    prepareContent();
                    questionDAO.createAnswerSheet(answerSheetId, listQuestions);
                }
            });
        } else {
            questionDAO.getAnswerSheet(answerSheetId, new QuestionDAO.FirebaseCallBack() {
                @Override
                public void onCallBack(ArrayList<Question> listQuestion) {
                    listQuestions = listQuestion;
                    prepareContent();
                }
            });
        }

    }

    private void prepareContent() {
        mockingTestAdapter = new MockingTestAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                listQuestions, status, answerSheetId);
        viewPager.setAdapter(mockingTestAdapter);

        tvCurrentQuestion.setText("1");
        tvTotalQuestion.setText(listQuestions.size() + "");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCurrentQuestion.setText(String.valueOf(position + 1));
                if (position == 0) {
                    tvBack.setVisibility(View.GONE);
                    tvNext.setVisibility(View.VISIBLE);
                    tvSubmit.setVisibility(View.GONE);
                    tvFinishReview.setVisibility(View.GONE);
                } else if (position == (listQuestions.size() - 1)) {
                    tvBack.setVisibility(View.VISIBLE);
                    tvNext.setVisibility(View.GONE);
                    if (status.equalsIgnoreCase(REVIEW_TEST_STATUS)) {
                        tvSubmit.setVisibility(View.GONE);
                        tvFinishReview.setVisibility(View.VISIBLE);
                    } else {
                        tvFinishReview.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvBack.setVisibility(View.VISIBLE);
                    tvNext.setVisibility(View.VISIBLE);
                    tvSubmit.setVisibility(View.GONE);
                    tvFinishReview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void onNextClick(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private void onBackClick(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    private void onSubmitClick(View view) {
        Intent intent = new Intent(this, MockingTestActivity.class);
        intent.putExtra("topic_code", topicCode);
        intent.putExtra("status", REVIEW_TEST_STATUS);
        intent.putExtra("answer_sheet_id", answerSheetId);
        startActivity(intent);
    }

    private void onFinishReviewClick(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public String convertToPath(String email) {
        return email.replace('.', '_')
                .replace('#', '_')
                .replace('$', '_')
                .replace('[', '_')
                .replace(']', '_');
    }


    private void fakeDataBase() {
        ArrayList<Question> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Question question = new Question(i + "",
                    "hello" + i,
                    "answer1",
                    "answer2",
                    "answer3",
                    "answer4",
                    1,
                    0);
            list.add(question);
        }
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Question").child(topicCode);
        for (Question question : list) {
            dbRef.child(question.getQuestionId()).setValue(question);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocking_test);
        bindingView();
        bindingAction();
    }

}