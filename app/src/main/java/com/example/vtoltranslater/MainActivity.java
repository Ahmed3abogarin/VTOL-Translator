package com.example.vtoltranslater;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    // Widgets
    private Spinner fromSpinner, toSpinner;
    private EditText sourceET;
    private TextView translatedTV;
    private Button btn;

    // Source Array of Strings - Spinners -Data
    private final String[] fromLanguage = {
            "from","English","Spanish","French","Arabic","Chinese","Hindi","German"
    };
    private final String[] toLanguage = {
            "to","English","Spanish","French","Arabic","Chinese","Hindi","German"
    };

    // data for Permission
    private static final int REQUEST_CODE = 1;
    String languageCode, fromLanguageCode, toLanguageCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.spinner1);
        toSpinner = findViewById(R.id.spinner2);
        sourceET = findViewById(R.id.sourceET);
        translatedTV = findViewById(R.id.translatedTV);
        btn = findViewById(R.id.button);


        // Spinner 1 (From language)
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = GetLanguageCode(fromLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this,
                R.layout.spinner_item,fromLanguage);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        // Spinner 2 (To language)
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = GetLanguageCode(toLanguage[i]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,
                R.layout.spinner_item,toLanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");

                if (sourceET.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,
                            "Text field can not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (fromLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this,
                            "Please select from language", Toast.LENGTH_SHORT).show();
                }
                else if (toLanguageCode.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please select to language", Toast.LENGTH_SHORT).show();
                }
                else {
                    TranslateText(fromLanguageCode, toLanguageCode,sourceET.getText().toString());
                }
            }
        });

    }

    private String GetLanguageCode(String language) {
        String languageCode;

        switch (language){
            case "English" :
                languageCode = TranslateLanguage.ENGLISH;
                break;
            case "Spanish" :
                languageCode = TranslateLanguage.SPANISH;
                break;
            case "French" :
                languageCode = TranslateLanguage.FRENCH;
                break;
            case "Arabic" :
                languageCode = TranslateLanguage.ARABIC;
                break;
            case "Chinese" :
                languageCode = TranslateLanguage.CHINESE;
                break;
            case "Hindi" :
                languageCode = TranslateLanguage.HINDI;
                break;
            case "German" :
                languageCode = TranslateLanguage.GERMAN;
                break;
            default :
                languageCode = "";

        }
        return languageCode;
    }

    public void TranslateText(String fromLanguageCode, String toLanguageCode, String src){

        translatedTV.setText("");
        try {
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode).build();

            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    translatedTV.setText("Translating....");

                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            translatedTV.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Failed to translate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Failed to download the language", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}