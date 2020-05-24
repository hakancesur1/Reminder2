package com.example.reminder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.util.TimeZone;

import static android.provider.CalendarContract.CalendarCache.URI;

public class NewTask extends AppCompatActivity {

    EditText title,detail;
    TextView dateTime;
    CheckBox isDone;
    Button date,share;
    RadioButton birthDay,activity,notice,meeting;
    String category;

    public static final String MyPreferences = "MyPrefs";
    public static final String ScreenModePreferences="ScreenModeKey";
    public static final String SnoozeTimePreferences="SnoozeTimeKey";
    public static final String RemindTimePreferences="RemindTimeKey";
    SharedPreferences sharedPreferences;

    int id;
    Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        db=new Db(NewTask.this);

        title=(EditText)findViewById(R.id.titleEditText);
        detail=(EditText)findViewById(R.id.detailEditText);
        isDone=(CheckBox)findViewById(R.id.doneCheckBox);
        date=(Button)findViewById(R.id.dateButton);
        dateTime=(TextView)findViewById(R.id.dateTimeTextView);
        birthDay=(RadioButton)findViewById(R.id.birthDayRadioButton);
        activity=(RadioButton)findViewById(R.id.activityRadioButton);
        notice=(RadioButton)findViewById(R.id.noticeRadioButton);
        meeting=(RadioButton)findViewById(R.id.meetingRadioButton);
        share=(Button)findViewById(R.id.shareButton);

        sharedPreferences=getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);

        String value = sharedPreferences.getString(ScreenModePreferences, "");
        if(value.equalsIgnoreCase("light"))
        {
            ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutThree);
            constraintLayout.setBackgroundColor(Color.parseColor("#E8F4FA"));
        }
        else {
            ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutThree);
            constraintLayout.setBackgroundColor(Color.parseColor("#141E57"));
        }

        id = getIntent().getIntExtra("id", -1);

        if(id!=-1){
            Notes updatedNote =  new Notes();
            updatedNote=db.getNoteByID(id);

            title.setText(updatedNote.getTitle());
            detail.setText(updatedNote.getDetail());

            if(updatedNote.getCategory().equalsIgnoreCase("Birthday"))
                birthDay.setChecked(true);
            else if (updatedNote.getCategory().equalsIgnoreCase("Activity"))
                activity.setChecked(true);
            else if (updatedNote.getCategory().equalsIgnoreCase("Notice"))
                notice.setChecked(true);
            else if (updatedNote.getCategory().equalsIgnoreCase("Meeting"))
                meeting.setChecked(true);

            if(updatedNote.getIsDone())
                isDone.setChecked(true);

            dateTime.setText(Notes.convertGregorianToDate(updatedNote.getTime()));

        }

        final Button backButton = (Button)findViewById(R.id.backButton);
        final Button saveButton = (Button)findViewById(R.id.saveButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent = new Intent(NewTask.this,MainActivity.class);
                startActivity(AndroidIntent);
            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        final Calendar newDate = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {


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
                                    Toast.makeText(NewTask.this,"Geçersiz Zaman",Toast.LENGTH_SHORT).show();

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

                note.setTitle(title.getText().toString());
                note.setDetail(detail.getText().toString());
                note.setCategory(category);
                note.setIsDone(isDone.isChecked());
                note.setTime(newDate);

                if(note.getTitle().equalsIgnoreCase(""))
                {
                    Toast.makeText(NewTask.this,"You must enter a title",Toast.LENGTH_SHORT).show();
                }
                else {

                    if (id == -1)
                        db.insertNotes(note);
                    else
                        db.updateReminderNote(id, title.getText().toString(), detail.getText().toString(), newDate.getTimeInMillis(), category, note.getIsDone() ? 1 : 0);

                    note.setId(db.getNoteByTime(newDate.getTimeInMillis()));

                    if (!note.getIsDone()) {
                        // Date remind = new Date(dateTime.getText().toString().trim());
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                        calendar.setTimeInMillis(note.getTime().getTimeInMillis());
                        calendar.set(Calendar.SECOND, 0);
                        Intent intent = new Intent(NewTask.this, AlarmNotifier.class);
                        // intent.putExtra("Message",note.getTitle());
                        //intent.putExtra("RemindDate", note.getTime().getTimeInMillis());
                        intent.putExtra("id", note.getId());

                        int repeatSetting = Integer.parseInt(sharedPreferences.getString(SnoozeTimePreferences, "0"));
                        long repeat = 0;
                        if(repeatSetting == 15){
                            repeat = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                        } else if (repeatSetting == 30) {
                            repeat = AlarmManager.INTERVAL_HALF_HOUR;
                        } else if (repeatSetting == 60){
                            repeat = AlarmManager.INTERVAL_HOUR;
                        }

                        int timeSetting = Integer.parseInt(sharedPreferences.getString(RemindTimePreferences, "0"));
                        long time = note.getTime().getTimeInMillis();
                        if(timeSetting !=0){
                            time = note.getTime().getTimeInMillis() - (timeSetting*60*1000);
                        }

                        PendingIntent intent1 = PendingIntent.getBroadcast(NewTask.this, note.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, repeat, intent1);
                    }


                    Toast.makeText(NewTask.this, "Kaydedildi", Toast.LENGTH_LONG).show();
               }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }
    public void sendEmail() {
        try {

            String mailSend="Başlık: " + title.getText() + "\n " + "Detay: " + detail.getText() + "\n " + "Zaman: " + dateTime.getText();
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("*/*"); //plain/text
            //emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mailSend});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hatırlatıcı - "+title.getText());
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mailSend);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
