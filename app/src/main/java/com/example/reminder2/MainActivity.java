package com.example.reminder2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Notes> notes = new ArrayList<>();

    Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new Db(MainActivity.this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        try {
            notes = db.getAllNotes();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "Anımsatıcı listesine ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        NoteAdapter noteAdapter = new NoteAdapter(this, notes);
        recyclerView.setAdapter(noteAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final Button addNewButton = (Button)findViewById(R.id.addNew_Button);
        final Button settingsButton=(Button)findViewById(R.id.settingsButton);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent=new Intent(MainActivity.this,NewTask.class);
                Toast.makeText(MainActivity.this,"Add New",Toast.LENGTH_LONG).show();
                startActivity(AndroidIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent=new Intent(MainActivity.this,Settings.class);
                Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_LONG).show();
                startActivity(AndroidIntent);
            }
        });
    }
}
