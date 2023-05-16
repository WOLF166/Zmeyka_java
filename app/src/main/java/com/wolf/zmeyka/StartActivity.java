package com.wolf.zmeyka;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity {

    Button btn_start_game, all_rec;
    EditText username_text;
    public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        btn_start_game = findViewById(R.id.btn_start);
        btn_start_game.setOnClickListener(v -> start());

        all_rec = findViewById(R.id.all_records);
        all_rec.setOnClickListener(v -> v_records());
        username_text = findViewById(R.id.username_edit);


    }

    private void v_records() {
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }

    private void start() {
        name = username_text.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name );
        startActivity(intent);

    }


}