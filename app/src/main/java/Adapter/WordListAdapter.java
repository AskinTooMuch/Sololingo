package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sololingo.EditWordActivity;
import com.example.sololingo.R;
import com.example.sololingo.ViewWordsActivity;

import java.util.ArrayList;

import Bean.Word;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final ArrayList<Word> mWordList;
    LayoutInflater mInflater;
    Context context;

    public WordListAdapter(Context context, ArrayList<Word> wordList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    @NonNull
    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create view from layout
        View mItemView = mInflater.inflate(
                R.layout.list_word_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, int position) {
        // Retrieve the data for that position
        int mWordId = mWordList.get(position).getId();
        String mWord = mWordList.get(position).getWord();
        String mWordDef = mWordList.get(position).getDefinition();
        int mSubjectId = mWordList.get(position).getSubjectId();
        String image = mWordList.get(position).getImage();

        holder.word= new Word(mWordId,mSubjectId,mWord,mWordDef,image);
        // Add the data to the view
        holder.wordItem.setText(mWord);
        holder.wordItemDef.setText(mWordDef);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final WordListAdapter mAdapter;
        protected TextView wordItem;
        protected TextView wordItemDef;
        protected Word word;

        public WordViewHolder(@NonNull View itemView, WordListAdapter adapter) {
            super(itemView);
            wordItem = itemView.findViewById(R.id.tvWord);
            wordItemDef = itemView.findViewById(R.id.tvWordDef);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditWordActivity.class);
            intent.putExtra("word",word);
            context.startActivity(intent);
        }
    }

}
