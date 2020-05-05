package com.example.reminder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addNewButton = (Button)findViewById(R.id.addNew_Button);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AndroidIntent=new Intent(MainActivity.this,NewTask.class);
                Toast.makeText(MainActivity.this,"Add New",Toast.LENGTH_LONG).show();
                startActivity(AndroidIntent);
            }
        });
    }
}
