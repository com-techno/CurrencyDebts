package ru.techno.currencydebts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static MyDatabase myDatabase;
    static MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fab).setOnClickListener(this);
        myAdapter = new MyAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        myAdapter.update();
        myDatabase = new MyDatabase(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, DebtActivity.class);
        intent.putExtra("flag", false);
        startActivity(intent);
    }
}

