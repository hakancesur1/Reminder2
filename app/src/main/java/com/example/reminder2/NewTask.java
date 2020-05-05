package com.example.reminder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        final Button backButton = (Button)findViewById(R.id.back_Button);

        final Button saveButton = (Button)findViewById(R.id.save_Button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent = new Intent(NewTask.this,MainActivity.class);
                Toast.makeText(NewTask.this,"Back",Toast.LENGTH_LONG).show();
                startActivity(AndroidIntent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewTask.this,"Save",Toast.LENGTH_LONG).show();
            }
        });
    }
}
