package Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sololingo.MockingTestFragment;

import java.util.ArrayList;

import Bean.Question;

public class MockingTestAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Question> listQuestion;
    private String status,answerSheetId;

    public MockingTestAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Question> list,String status,String answerSheetId) {
        super(fm, behavior);
        this.listQuestion = list;
        this.status = status;
        this.answerSheetId = answerSheetId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (listQuestion == null || listQuestion.isEmpty()) {
            return null;
        } else {
            Question question = listQuestion.get(position);
            MockingTestFragment mockingTestFragment = new MockingTestFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("question_object",question);
            bundle.putString("status",status);
            bundle.putString("answer_sheet_id",answerSheetId);
            mockingTestFragment.setArguments(bundle);
            return mockingTestFragment;
        }
    }

    @Override
    public int getCount() {
        if (listQuestion != null) {
            return listQuestion.size();
        }
        return 0;
    }
}
