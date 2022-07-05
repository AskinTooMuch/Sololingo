package com.example.sololingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTranslate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTranslate extends Fragment {

    private static final int REQUEST_PERMISSION_CODE = 1;
    // TODO: Rename parameter arguments, choose names that match
    private int from = 1;
    private int to = 2;
    public Translator languageTranslator;
    private boolean translatorFlag = false;
    private boolean downloadedModel = false;
    private EditText edtSourceText;
    private ImageButton ibtnMic, ibtnSwitch;
    private TextView tvTranslateResult;
    private Button btnTranslate;
    private Spinner spinLanguageFrom, spinLanguageTo;
    private String[] languages = {"Tiếng Việt","English","日本語","한국인"};
    private String[] codes = {
            TranslateLanguage.VIETNAMESE,
            TranslateLanguage.ENGLISH,
            TranslateLanguage.JAPANESE,
            TranslateLanguage.KOREAN};

    private void bindingView(View v){
        edtSourceText = v.findViewById(R.id.edtSourceText);
        ibtnMic = v.findViewById(R.id.ibtnMic);
        ibtnSwitch = v.findViewById(R.id.ibtnSwitch);
        tvTranslateResult = v.findViewById(R.id.tvTranslateResult);
        btnTranslate = v.findViewById(R.id.btnTranslate);
        //Spinner
        spinLanguageFrom = v.findViewById(R.id.spinLanguageFrom);
        spinLanguageTo = v.findViewById(R.id.spinLanguageTo);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new  ArrayAdapter(v.getContext(),
                 R.layout.spinner_item, languages);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinLanguageFrom.setAdapter(adapter);
        spinLanguageTo.setAdapter(adapter);
    }

    private void bindingAction(View v){
        //Translate
        btnTranslate.setOnClickListener(this::translate);
        //Switch
        ibtnSwitch.setOnClickListener(this::switchLanguages);
        //Get from language
        spinLanguageFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = position;
                Log.d("TAG", "onItemSelected: from = "+from);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Get to language
        spinLanguageTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = position;
                Log.d("TAG", "onItemSelected: to = "+to);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Use mic
        ibtnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent micIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                micIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Let us hear your beautiful voice");
                try {
                    startActivityForResult(micIntent, REQUEST_PERMISSION_CODE);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (resultCode == -1 && data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edtSourceText.setText(result.get(0));
            }
        }
    }

    private void switchLanguages(View view) {
        //Switch selection
        int temp = from;
        from = to;
        to = temp;
        spinLanguageFrom.setSelection(from);
        spinLanguageTo.setSelection(to);
        //Switch translate text
        CharSequence textTemp = edtSourceText.getText();
        edtSourceText.setText(tvTranslateResult.getText());
        tvTranslateResult.setTextColor(getResources().getColor(R.color.black));
        tvTranslateResult.setText(textTemp);
    }

    private void translate(View view) {
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(codes[from])
                        .setTargetLanguage(codes[to])
                        .build();
        languageTranslator = Translation.getClient(options);
        if (!edtSourceText.getText().toString().isEmpty()) {
            tvTranslateResult.setTextColor(getResources().getColor(R.color.red));
            tvTranslateResult.setText("Please wait while we making calls to the Oracles");
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
                                    tvTranslateResult.setTextColor(getResources().getColor(R.color.red));
                                    tvTranslateResult.setText(
                                            "Model could not be downloaded because internet connection or other internal error.");
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
                                        tvTranslateResult.setTextColor(getResources().getColor(R.color.black));
                                        tvTranslateResult.setText(o.toString());
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        tvTranslateResult.setTextColor(getResources().getColor(R.color.red));
                                        tvTranslateResult.setText(
                                                "Something went wrong. Try again when the model finish downloading");
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
        spinLanguageFrom.setSelection(1);
        spinLanguageTo.setSelection(2);
    }

}