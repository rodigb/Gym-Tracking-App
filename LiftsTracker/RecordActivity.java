package com.example.LiftsTracker;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class RecordActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    DatabaseHelper myDb;
    EditText editBench,editSquat,editDeadlift,editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnviewUpdate;
    Button btnDelete;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        myDb = new DatabaseHelper(this);

        mTextView = (TextView)findViewById(R.id.textViewR);

        editBench = (EditText)findViewById(R.id.editText_bench);
        editSquat = (EditText)findViewById(R.id.editText_squat);
        editDeadlift= (EditText)findViewById(R.id.editText_deadlift);
        btnAddData = (Button) findViewById(R.id.button_add);
        btnviewAll = (Button) findViewById(R.id.button_viewAll);
        btnviewUpdate = (Button) findViewById(R.id.button_update);
        btnDelete = (Button) findViewById(R.id.button_delete);
        editTextId = (EditText)findViewById(R.id.editText_Id);
        AddData();
        viewAll();
        UpdateData();


        Button button_remind;
        button_remind = (Button)findViewById(R.id.button_remind);
        button_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
    }

    public void UpdateData(){
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isUpdated =  myDb.updateData(editTextId.getText().toString(),
                                editBench.getText().toString(),
                                editSquat.getText().toString(),editDeadlift.getText().toString());
                        if(isUpdated == true)
                            Toast.makeText(RecordActivity.this,"Data updated", Toast.LENGTH_LONG).show();//updates info about users lifts
                        else
                            Toast.makeText(RecordActivity.this,"Data not updated", Toast.LENGTH_LONG).show();

                    }
                }
        );
    }



    public void AddData(){

        btnAddData.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    boolean isInserted = myDb.insertData(editBench.getText().toString(),
                            editSquat.getText().toString(),
                            editDeadlift.getText().toString() );
                if(isInserted ==true)
                        Toast.makeText(RecordActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();//adds the info and tells user if it has added or not
                        else
                            Toast.makeText(RecordActivity.this,"Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                });
        }

        public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            //show message
                            showMessage("Error","No data");//allows user to view data
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()){
                            buffer.append("Week : "+ res.getString(0)+"\n");
                            buffer.append("Bench : "+ res.getString(1)+"\n");
                            buffer.append("Squat : "+ res.getString(2)+"\n");
                            buffer.append("Deadlift : "+ res.getString(3)+"\n\n");
                        }
                        showMessage("Data",buffer.toString());
                    }
                }
        );
        }
public void showMessage(String title,String Message){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(true);
    builder.setTitle(title);
    builder.setMessage(Message);//method to show message
    builder.show();
}



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);//Calender made

        updateTime(c);
        startNotification(c);

    }

    private void updateTime(Calendar c){
        String timeText = "Notification set for:";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTextView.setText(timeText);//lets user see what time alarm was set to

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void startNotification(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);//allows notification to start

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);
    }
    }

