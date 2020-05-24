package com.example.reminder2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class Settings extends AppCompatActivity {

    public static final String MyPreferences = "MyPrefs";
    public static final String NotificationTypePreferences="NotificationKey";
    public static final String ScreenModePreferences="ScreenModeKey";
    public static final String SnoozeTimePreferences="SnoozeTimeKey";
    public static final String RemindTimePreferences="RemindTimeKey";
    SharedPreferences sharedPreferences;
    String uriString;

    Switch screenMode;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button notificationButton = (Button)findViewById(R.id.notificationButton);

        final EditText snoozeEditText=(EditText)findViewById(R.id.snoozeEditText);
        final EditText remindTimeEditText=(EditText)findViewById(R.id.remindTimeEditText);

        sharedPreferences=getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);

        snoozeEditText.setText(sharedPreferences.getString(SnoozeTimePreferences,""));
        remindTimeEditText.setText(sharedPreferences.getString(RemindTimePreferences,""));

        screenMode=(Switch)findViewById(R.id.screenModeSwitch);



        String value = sharedPreferences.getString(ScreenModePreferences, "");
        if(value.equalsIgnoreCase("light"))
        {
            ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutOne);
            constraintLayout.setBackgroundColor(Color.parseColor("#E8F4FA"));
            screenMode.setChecked(true);
        }
        else {
            ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutOne);
            constraintLayout.setBackgroundColor(Color.parseColor("#141E57"));
        }

        screenMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b=screenMode.isChecked();
                ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutOne);
                if(b)
                    constraintLayout.setBackgroundColor(Color.parseColor("#E8F4FA"));
                else
                    constraintLayout.setBackgroundColor(Color.parseColor("#141E57"));
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                Settings.this.startActivityForResult(intent, 999);
            }
        });

        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent androidIntent=new Intent(Settings.this,MainActivity.class);
                startActivity(androidIntent);
            }
        });

        Button saveButton = (Button)findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences=getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(NotificationTypePreferences, uriString);


                String screenModeString="";
                if(screenMode.isChecked())
                    screenModeString="light";
                else
                    screenModeString="dark";

                String snoozeString=snoozeEditText.getText().toString();

                if
                (
                        snoozeString.equalsIgnoreCase("0")
                                ||snoozeString.equalsIgnoreCase("15")
                                ||snoozeString.equalsIgnoreCase("30")
                                ||snoozeString.equalsIgnoreCase("60")
                ) {

                    editor.putString(ScreenModePreferences, screenModeString);
                    editor.putString(SnoozeTimePreferences, snoozeEditText.getText().toString());
                    editor.putString(RemindTimePreferences, remindTimeEditText.getText().toString());

                    editor.commit();
                    Toast.makeText(Settings.this, "Kaydedildi.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Settings.this, "Girilen Erteleme Zamanı 0 15 30 veya 60 Değerlerinden Biri Olmalıdır!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            uriString=String.valueOf(uri);
            Toast.makeText(Settings.this,"Seçim yapıldı. ",Toast.LENGTH_SHORT).show();
            if (uri != null) {
                String ringTonePath = uri.toString();
            }
        }




    }
}
