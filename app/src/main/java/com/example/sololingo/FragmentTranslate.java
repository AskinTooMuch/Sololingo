package com.example.sololingo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTranslate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTranslate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static String from = "EN";
    private static String to = "JA";
    public Translator languageTranslator;
    private boolean translatorFlag = false;
    private boolean downloadedModel = false;
    private EditText edtSourceText;
    private ImageButton ibtnMic, ibtnSwitch;
    private TextView tvTranslateResult;
    private Button btnTranslate;
    private Spinner spinLanguageFrom, spinLanguageTo;

    private void bindingView(View v){
        edtSourceText = v.findViewById(R.id.edtSourceText);
        ibtnMic = v.findViewById(R.id.ibtnMic);
        ibtnSwitch = v.findViewById(R.id.ibtnSwitch);
        tvTranslateResult = v.findViewById(R.id.tvTranslateResult);
        btnTranslate = v.findViewById(R.id.btnTranslate);
        spinLanguageFrom = v.findViewById(R.id.spinLanguageFrom);
        spinLanguageTo = v.findViewById(R.id.spinLanguageTo);
    }

    private void bindingAction(View v){
        btnTranslate.setOnClickListener(this::translate);
    }

    private void translate(View view) {
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.JAPANESE)
                        .build();
        languageTranslator = Translation.getClient(options);
        if (!edtSourceText.getText().toString().isEmpty()) {
            tvTranslateResult.setText("SYSTEM: Please wait while we making calls to the Oracles");

            String source = edtSourceText.getText().toString();
            translatorFlag = true;
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            languageTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //Set flag
                                    translatorFlag = true;
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
                languageTranslator.translate(source)
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

    // TODO: Rename and change types of parameters

    public FragmentTranslate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment_Translate.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTranslate newInstance(String param1, String param2) {
        FragmentTranslate fragment = new FragmentTranslate();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__translate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView(view);
        bindingAction(view);
    }
}