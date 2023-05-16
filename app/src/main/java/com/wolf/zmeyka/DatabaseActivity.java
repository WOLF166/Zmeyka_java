package com.wolf.zmeyka;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wolf.zmeyka.Adapter.MyAdapter;
import com.wolf.zmeyka.ForClasses.Databaseclass;
import com.wolf.zmeyka.Model.Record;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {

    ListView ll;
    Databaseclass databaseclass;
    ArrayList<Record> arrayList;
    MyAdapter myAdapter;
    Button b_tth_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ll = (ListView) findViewById(R.id.listView);
        databaseclass = new Databaseclass(this);
        arrayList = new ArrayList<>();
        loadDataInListView();
        b_tth_l = findViewById(R.id.b_tothe_lobby);
        b_tth_l.setOnClickListener(v -> go_back());
    }

    private void loadDataInListView()
    {
        arrayList = databaseclass.getAllData();
        myAdapter = new MyAdapter(this, arrayList);
        ll.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    private void go_back() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

}