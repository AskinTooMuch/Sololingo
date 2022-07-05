package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sololingo.R;
import com.example.sololingo.ViewWordsActivity;

import java.util.ArrayList;

import Bean.Subject;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder> {
    private final ArrayList<Subject> mSubjectList;
    LayoutInflater mInflater;
    Context context;
    public SubjectListAdapter(Context context, ArrayList<Subject> subjectList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mSubjectList = subjectList;
    }


    @NonNull
    @Override
    public SubjectListAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create view from layout
        View mItemView = mInflater.inflate(
                R.layout.list_subject_item, parent, false);
        return new SubjectListAdapter.SubjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListAdapter.SubjectViewHolder holder, int position) {
        // Retrieve the data for that position
        String mSubject= mSubjectList.get(position).getName();
        String mSubjectUId = mSubjectList.get(position).getuId();
        int mSubjectId = mSubjectList.get(position).getId();

        // Add the data to the view
        holder.subject = new Subject(mSubjectId,mSubjectUId,mSubject);
        holder.subjectItem.setText(mSubject);
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final SubjectListAdapter mAdapter;
        protected TextView subjectItem;
        protected int position;
        protected Subject subject;


        public SubjectViewHolder(@NonNull View itemView, SubjectListAdapter adapter) {
            super(itemView);
            subjectItem = itemView.findViewById(R.id.tvSubject);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ViewWordsActivity.class);
            intent.putExtra("subject",subject);
            context.startActivity(intent);
        }
    }
}
