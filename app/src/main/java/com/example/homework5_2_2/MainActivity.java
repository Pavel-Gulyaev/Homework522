package com.example.homework5_2_2;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner mLanguageSpinner;
    private Button mOkBtn;
    private SharedPreferences sharedPref;
    private EditText loginEt;
    private EditText passwordEt;
    private Button loginBtn;
    private Button regBtn;
    private CheckBox checkBox;
    Locale locale = new Locale("ru");
    public static final String FILE_NAME = "data.txt";
    private static File dataFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("check", MODE_PRIVATE);
        initViews();
    }

    private void initViews() {
        mLanguageSpinner = findViewById(R.id.languageSpinner);
        loginEt = findViewById(R.id.login);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        regBtn = findViewById(R.id.registration_btn);

        checkBox = findViewById(R.id.checkbox);
        checkBox.setChecked(sharedPref.getBoolean("check", false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.putBoolean("check", checkBox.isChecked());
                editor.apply();
            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(loginEt.getText().toString(), passwordEt.getText().toString());
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(loginEt.getText().toString(), passwordEt.getText().toString());
            }
        });

        mOkBtn = findViewById(R.id.okBtn);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLanguageSpinner.getSelectedItemPosition() == 0){
                    locale = new Locale("ru");
                }
                if (mLanguageSpinner.getSelectedItemPosition() == 1){
                    locale = new Locale("en-rGB");
                }


                Configuration config = new Configuration();
                config.setLocale(locale);
                getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                recreate();
            }
        });
        initLanguageSpinner();

    }

    private void initLanguageSpinner() {
        ArrayAdapter<CharSequence> adapterLanguage = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLanguageSpinner.setAdapter(adapterLanguage);

        mLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] language = getResources().getStringArray(R.array.language);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private File getDataFolder(){
        return getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }
    private File getDataFile(){
        return new File(getDataFolder(), FILE_NAME);
    }

    private void saveData(String login, String password){
        try{
            if(checkBox.isChecked()){
                dataFile = getDataFile();
                Writer writer = new FileWriter(dataFile);
                writer.write(login);
                writer.write("\n");
                writer.write(password);
                writer.write("\n");
                writer.close();
            } else {
                FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                writer.write(login);
                writer.write("\n");
                writer.write(password);
                writer.write("\n");
                writer.close();
            }
        } catch (Exception e){

        }

    }

    private void check(String login, String password){
        try {
            if(checkBox.isChecked()){
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(dataFile), StandardCharsets.UTF_8));
                String loginText = "";
                String passwordText = "";
                String line;
                while (((line = reader.readLine()) != null)){
                    loginText = line;
                    passwordText = reader.readLine();
                    if (loginText.equals(login) && passwordText.equals(password)){
                        Toast.makeText(MainActivity.this,
                                "Добро пожаловать " + login,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {

                FileInputStream fileInputStream = openFileInput(FILE_NAME);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String loginText = "";
                String passwordText = "";
                String line;
                while (((line = reader.readLine()) != null)){
                    loginText = line;
                    passwordText = reader.readLine();
                    if (loginText.equals(login) && passwordText.equals(password)){
                        Toast.makeText(MainActivity.this,
                                "Добро пожаловать " + login,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(MainActivity.this,
                    "Не найдено пользователя с таким логином",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
