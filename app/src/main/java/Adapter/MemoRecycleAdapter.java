package Adapter;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sololingo.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import Bean.Memo;

public class MemoRecycleAdapter extends FirestoreRecyclerAdapter<Memo, MemoRecycleAdapter.MemoViewHolder> {
    private OnMemoClickListener mOnMemoClickListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MemoRecycleAdapter(@NonNull FirestoreRecyclerOptions<Memo> options, OnMemoClickListener onMemoClickListener) {
        super(options);
        mOnMemoClickListener = onMemoClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull MemoViewHolder holder, int position, @NonNull Memo memo) {
        holder.tvMemoCardTitle.setText(memo.getTitle());
        CharSequence createDate = DateFormat.format("EEEE, MMM d, yyyy h:mm:ss a", memo.getCreateDate());
        holder.tvMemoCardDate.setText(createDate);
        holder.memoId = getSnapshots().getSnapshot(position).getId();
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_memo_card,parent,false);
        return new MemoViewHolder(view, mOnMemoClickListener);
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String memoId;
        private TextView tvMemoCardTitle;
        private TextView tvMemoCardDate;
        private OnMemoClickListener onMemoClickListener;

        public MemoViewHolder(@NonNull View itemView, OnMemoClickListener onMemoClickListener) {
            super(itemView);

            tvMemoCardDate = itemView.findViewById(R.id.tvMemoCardDate);
            tvMemoCardTitle = itemView.findViewById(R.id.tvMemoCardTitle);
            this.onMemoClickListener = onMemoClickListener;
            Log.d("++++++++++++++++++", "onBindViewHolder: "+ memoId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMemoClickListener.onMemoClick(memoId);
        }
    }

    public interface OnMemoClickListener{
        void onMemoClick(String id);
    }
}
