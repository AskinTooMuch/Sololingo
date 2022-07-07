package Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sololingo.FragmentFlashcards;
import com.example.sololingo.FragmentMemo;
import com.example.sololingo.FragmentTranslate;
import com.example.sololingo.R;

public class TabAdapter  extends FragmentStatePagerAdapter {
    Context context;

    public TabAdapter(@NonNull FragmentManager fm, int behavior, Context nContext) {
        super(fm, behavior);
        context = nContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new FragmentTranslate();
                break;
            case 1:
                frag = new FragmentFlashcards();
                break;
            case 2:
                frag = new FragmentMemo();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = context.getString(R.string.tabTranslate);
                break;
            case 1:
                title = context.getString(R.string.tabWord);
                break;
            case 2:
                title = context.getString(R.string.tabMemo);
                break;
        }
        return title;
    }
}
