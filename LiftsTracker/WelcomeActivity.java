package com.example.LiftsTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class WelcomeActivity extends AppCompatActivity {

    private static final String FILE_NAME = "example.txt";

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mEditText = findViewById(R.id.introText);

    }

public void save(View view){
        String text = mEditText.getText().toString();
        FileOutputStream fos = null;

    try {
        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
        fos.write(text.getBytes());

        mEditText.getText().clear();
        Toast.makeText(this, "Saved at " + getFilesDir() +"/" + FILE_NAME, Toast.LENGTH_LONG).show();//saves user input
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (fos != null){
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    openActivity3();


}

public void load(View v){

        FileInputStream fis = null;

    try {
        fis = openFileInput(FILE_NAME);
        InputStreamReader isr = new InputStreamReader(fis);//loads user input
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String text;
        while((text = br.readLine()) != null){
            sb.append(text).append("\n");
        }

        mEditText.setText(sb.toString());

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    finally {
        if (fis != null){
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

    public void openActivity3(){
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }





}



