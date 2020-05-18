package com.example.reminder2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewTask extends AppCompatActivity {

    EditText title,detail;
    TextView dateTime;
    CheckBox isDone;
    Button date;
    RadioButton birthDay,activity,notice,meeting;
    String category;

    Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);


        title=(EditText)findViewById(R.id.titleEditText);
        detail=(EditText)findViewById(R.id.detailEditText);
        isDone=(CheckBox)findViewById(R.id.doneCheckBox);
        date=(Button)findViewById(R.id.dateButton);
        dateTime=(TextView)findViewById(R.id.dateTimeTextView);
        birthDay=(RadioButton)findViewById(R.id.birthDayRadioButton);
        activity=(RadioButton)findViewById(R.id.activityRadioButton);
        notice=(RadioButton)findViewById(R.id.noticeRadioButton);
        meeting=(RadioButton)findViewById(R.id.meetingRadioButton);




        final Button backButton = (Button)findViewById(R.id.backButton);
        final Button saveButton = (Button)findViewById(R.id.saveButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent = new Intent(NewTask.this,MainActivity.class);
                Toast.makeText(NewTask.this,"Back",Toast.LENGTH_LONG).show();
                startActivity(AndroidIntent);
            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(NewTask.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0)
                                    dateTime.setText(Notes.convertGregorianToDate(newDate));
                                else
                                    Toast.makeText(NewTask.this,"Invalid time",Toast.LENGTH_SHORT).show();

                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();

                    }
                },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Notes note = new Notes();
                Db db = new Db(NewTask.this);

                if(birthDay.isChecked())
                    category="Birthday";
                else if(activity.isChecked())
                    category="Activity";
                else if(notice.isChecked())
                    category="Notice";
                else if (meeting.isChecked())
                    category="Meeting";
                else
                    category="Other";

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateTime.getDrawingTime());

                note.setTitle(title.getText().toString());
                note.setDetail(detail.getText().toString());
                note.setCategory(category);
                note.setIsDone(isDone.isChecked());
                note.setTime(newCalendar);

                db.insertNotes(note);

                Toast.makeText(NewTask.this,"Save",Toast.LENGTH_LONG).show();
            }
        });
    }
}
