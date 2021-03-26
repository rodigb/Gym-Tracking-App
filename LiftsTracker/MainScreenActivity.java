package com.example.LiftsTracker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainScreenActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    private static final String FILE_NAME = "example.txt";

    Button standards_button;
    Button record_button;
    EditText mEditText;
    Button button_profile;
    ImageView userPhoto;
    private ImageView UserPhoto;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    Button buttonTimePicker;

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);






        ////

        mEditText = findViewById(R.id.editName);
        mEditText.setEnabled(false);

        standards_button = findViewById(R.id.strstandards);
        standards_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity2();
                    }
                }
        );
        record_button = findViewById(R.id.str_info);//on clicks created to navigate to other activities
        record_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity3();
                    }
                }
        );
        button_profile = findViewById(R.id.button_profile);
        button_profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity5();
                    }
                });


                Button buttonRequest = findViewById(R.id.Requestbtn); //Request button instruction, if clicked on and permission was granted, it'll say permission was already granted
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainScreenActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainScreenActivity.this, "You have already granted this permission!", Toast.LENGTH_LONG).show();

                }
                else{
                    requestStoragePermission(); //else run this
                }

            }
        });

load();//For loading internal storage to next page

        userPhoto = findViewById(R.id.userPhoto);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(MainScreenActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, "select picture"), PICK_IMAGE);//allows user to pick an image
                }else Toast.makeText(MainScreenActivity.this, "You don't have permission", Toast.LENGTH_LONG).show();



            }
        });

    }
    public void openActivity2(){
        Intent intent = new Intent(this, StandardsActivity.class);
        startActivity(intent);
    }
    public void openActivity3(){
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void openActivity5(){
        Intent intent = new Intent(this, DisplayUserGuideActivity.class);//methods for opening the different activities
        startActivity(intent);
    }

    public void load(){

        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null){//loads the internal storage
                sb.append(text).append("\n");
            }

            mEditText.setText("Welcome, " + sb.toString());//here is where the internal storage info is placed into the app

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

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){ // requests for permissions.
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is required")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {//gives usr permission to use storage
                            ActivityCompat.requestPermissions(MainScreenActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int which){
                            dialogInterface.dismiss();


                        }
                    })
                    .create().show();
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //Once the button is clicked and the dialogue box is opened
        if(requestCode == STORAGE_PERMISSION_CODE) {// The user will then click ok or cancel, and depending on what they click, they will either get a message saying
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);//permission granted or denied.
            Toast.makeText(this, "Click on avatar to see if permission is given!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){//for picking avatar image
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                userPhoto.setImageBitmap(bitmap);

            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

}
