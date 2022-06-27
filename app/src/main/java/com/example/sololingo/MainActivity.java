package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Set;

import Adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private EditText edtTranslateData;
    private ImageButton ibtnTranslate;
    private TextView tvTranslateResult;
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static String from = "EN";
    private static String to = "JA";
    public Translator en_jaTranslator;
    private boolean translatorFlag = false;
    private boolean downloadedModel = false;
    private ViewPager pager;
    private TabLayout tabLayout;

    protected void bindingView() {
        /*edtTranslateData = findViewById(R.id.edtTranslateData);
        ibtnTranslate = findViewById(R.id.ibtnTranslate);
        tvTranslateResult = findViewById(R.id.tvTranslateResult);*/
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.JAPANESE)
                        .build();
        en_jaTranslator = Translation.getClient(options);
    }

    protected void bindingAction() {
        ibtnTranslate.setOnClickListener(this::translate);
    }

    private void translate(View view) {
        if (!edtTranslateData.getText().toString().isEmpty()) {
            tvTranslateResult.setText("SYSTEM: Please wait while we making calls to the Oracles");

            String source = edtTranslateData.getText().toString();
            translatorFlag = true;
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            en_jaTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //Set flag
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Couldn't load
                                    tvTranslateResult.setText("SYSTEM: Model could not be downloaded because internet connection or other internal error.");
                                    translatorFlag = false;
                                }
                            });
            if (translatorFlag) {
                en_jaTranslator.translate(source)
                        .addOnSuccessListener(
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        //Success
                                        tvTranslateResult.setText(o.toString());
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        tvTranslateResult.setText("SYSTEM: Something went wrong.");
                                    }
                                });
            }
        }
    }

    private boolean checkIfModelAvailable(String from, String to) {
        RemoteModelManager modelManager = RemoteModelManager.getInstance();
        TranslateRemoteModel jaModel = new TranslateRemoteModel.Builder(TranslateLanguage.JAPANESE).build();
        return downloadedModel;
    }

    private void setTabLayout() {
        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        FragmentManager manager = getSupportFragmentManager();
        TabAdapter adapter = new TabAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bindingView();
        //bindingAction();
        setTabLayout();
    }


}