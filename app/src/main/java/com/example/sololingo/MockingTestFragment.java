package com.example.sololingo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import Bean.Question;
import DAO.QuestionDAO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MockingTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MockingTestFragment extends Fragment {

    private TextView tvQuestion;
    private View view;
    private String status,answerSheetId;
    private RadioGroup radioGroup;
    private RadioButton answer1,answer2,answer3,answer4;
    private Question question;

    private void bindingView(View view){
        tvQuestion = view.findViewById(R.id.tvQuestion);
        radioGroup = view.findViewById(R.id.radio_group);
        answer1 = view.findViewById(R.id.answer1);
        answer2 = view.findViewById(R.id.answer2);
        answer3 = view.findViewById(R.id.answer3);
        answer4 = view.findViewById(R.id.answer4);
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MockingTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MockingTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MockingTestFragment newInstance(String param1, String param2) {
        MockingTestFragment fragment = new MockingTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mocking_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView(view);
        bindingAction(view);
    }

    private void bindingAction(View view) {
        Bundle bundleReceive = getArguments();
        if(bundleReceive != null){
            question = (Question) bundleReceive.get("question_object");
            status = bundleReceive.getString("status");
            answerSheetId = bundleReceive.getString("answer_sheet_id");
            if(status.equalsIgnoreCase(MockingTestActivity.TAKE_TEST_STATUS)){
                prepareContent();
            }  else if(status.equalsIgnoreCase(MockingTestActivity.REVIEW_TEST_STATUS)){
                prepareContent();
                reviewQuestion();
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                QuestionDAO questionDAO = new QuestionDAO();
                switch (checkedId){
                    case R.id.answer1:
                        /*
                        Question answerQuestion = new Question(question.getQuestionId(),
                                question.getQuestionInfor(),
                                question.getAnswer1(),
                                question.getAnswer2(),
                                question.getAnswer3(),
                                question.getAnswer4(),
                                question.getRightAnswer(),
                                1);

                         */
                        question.setChosenAnswer(1);
                        questionDAO.updateAnswerSheet(answerSheetId,question);
                        break;
                    case R.id.answer2:
                        question.setChosenAnswer(2);
                        questionDAO.updateAnswerSheet(answerSheetId,question);
                        break;
                    case R.id.answer3:
                        question.setChosenAnswer(3);
                        questionDAO.updateAnswerSheet(answerSheetId,question);
                        break;
                    case R.id.answer4:
                        question.setChosenAnswer(4);
                        questionDAO.updateAnswerSheet(answerSheetId,question);
                        break;
                }
            }
        });
    }

    private void reviewQuestion() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
        int chosenAnswer = question.getChosenAnswer();
        switch (chosenAnswer){
            case 1:answer1.setTextColor(Color.RED);break;
            case 2:answer2.setTextColor(Color.RED);break;
            case 3:answer3.setTextColor(Color.RED);break;
            case 4:answer4.setTextColor(Color.RED);break;
        }
        int rightAnswer = question.getRightAnswer();
        switch (rightAnswer){
            case 1:answer1.setTextColor(Color.GREEN);break;
            case 2:answer2.setTextColor(Color.GREEN);break;
            case 3:answer3.setTextColor(Color.GREEN);break;
            case 4:answer4.setTextColor(Color.GREEN);break;
        }
    }

    private void prepareContent() {
        tvQuestion.setText(question.getQuestionInfor());
        answer1.setText(question.getAnswer1());
        answer2.setText(question.getAnswer2());
        answer3.setText(question.getAnswer3());
        answer4.setText(question.getAnswer4());
    }
}