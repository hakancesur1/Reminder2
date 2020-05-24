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

public class FilterTasks extends AppCompatActivity {

    ArrayList<Notes> notes;
    ArrayList<Notes> notesShow ;
    int type = 0;
    Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_tasks);

        notes = new ArrayList<>();
        notesShow = new ArrayList<>();

        db = new Db(FilterTasks.this);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notelistrecyclerview);

        try {
            notes = db.getAllNotes();
        } catch (ParseException e) {
            Toast.makeText(FilterTasks.this, "Anımsatıcı listesine ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        type = getIntent().getIntExtra("listType", 0);
        notesShow = Notes.getRemindersWithType(notes,type);

        NoteAdapter noteAdapter = new NoteAdapter(this, notesShow);
        recyclerView.setAdapter(noteAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Button backButton = (Button)findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent=new Intent(FilterTasks.this,MainActivity.class);
                startActivity(AndroidIntent);
            }
        });
    }
}