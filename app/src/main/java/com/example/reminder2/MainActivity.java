package com.example.reminder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    NoteAdapter noteAdapter;
    ArrayList<Notes> notes=new ArrayList<>();

    public static final String MyPreferences = "MyPrefs";
    public static final String ScreenModePreferences="ScreenModeKey";
    SharedPreferences sharedPreferences;

    TextView scrollingText;

    Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addNewButton = (Button)findViewById(R.id.addNew_Button);
        final Button settingsButton=(Button)findViewById(R.id.settingsButton);
        final RadioButton today=(RadioButton)findViewById(R.id.todayRadioButton);
        final RadioButton week=(RadioButton)findViewById(R.id.weekRadioButton);
        final RadioButton month=(RadioButton)findViewById(R.id.monthRadioButton);

        final RadioGroup group=(RadioGroup)findViewById(R.id.radioGroup) ;


        sharedPreferences=getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);

        String value = sharedPreferences.getString(ScreenModePreferences, "");
        if(value.equalsIgnoreCase("light"))
        {
            ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutTwo);
            constraintLayout.setBackgroundColor(Color.parseColor("#E8F4FA"));
            group.setBackgroundColor(Color.parseColor("#E8F4FA"));
        }
        else {
            ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraitLayoutTwo);
            constraintLayout.setBackgroundColor(Color.parseColor("#141E57"));
            group.setBackgroundColor(Color.parseColor("#141E57"));
        }


        db = new Db(MainActivity.this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        try {
            notes = db.getAllNotes();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "Anımsatıcı listesine ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilterTasks.class);
                intent.putExtra("listType", 1);
                startActivity(intent);
            }
        });

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilterTasks.class);
                intent.putExtra("listType", 2);
                startActivity(intent);
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilterTasks.class);
                intent.putExtra("listType", 3);
                startActivity(intent);
            }
        });


        ArrayList<Integer> mlist =new ArrayList<Integer>();
        mlist=Notes.getRemindersDetails(notes);

        scrollingText = (TextView)findViewById(R.id.scrollingtext);
        scrollingText.setText("Bugünkü Aktivite Sayısı: " +mlist.get(0).toString() +"/"+mlist.get(4)
                +" Haftalık Aktivite Sayısı: "+mlist.get(1).toString()+"/"+mlist.get(5)
                +" Aylık Aktivite Sayısı: "+mlist.get(2).toString()+"/"+mlist.get(6)
                +" Toplam Aktivite Sayısı: "+mlist.get(3).toString()+"/"+mlist.get(7));
        scrollingText.setSelected(true);

        noteAdapter = new NoteAdapter(this, notes);
        recyclerView.setAdapter(noteAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent androidIntent=new Intent(MainActivity.this,NewTask.class);
                androidIntent.putExtra("id",-1);
                startActivity(androidIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent=new Intent(MainActivity.this,Settings.class);
                startActivity(AndroidIntent);
            }
        });

    }
}
