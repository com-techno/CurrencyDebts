package ru.techno.currencydebts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static MyDatabase myDatabase;
    static MyAdapter myAdapter;
    static Spinner spinnerSorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fab).setOnClickListener(this);
        spinnerSorting = findViewById(R.id.spinnerSorting);
        myAdapter = new MyAdapter(this);


        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        myAdapter.update();
        myDatabase = new MyDatabase(this);
        bindAdapterToSpinnerCurrency();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, DebtActivity.class);
        intent.putExtra("flag", false);
        startActivity(intent);
    }

    private void bindAdapterToSpinnerCurrency(){
        String[] sSortingVariables = {"Имени↓", "Имени↑", "Долгу↓", "Долгу↑", "По ID"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.curr, R.id.currencyText, sSortingVariables);
        adapter.setDropDownViewResource(R.layout.curr);
        spinnerSorting.setAdapter(adapter);
        spinnerSorting.setPrompt("Сортировать по...");
        spinnerSorting.setSelection(3);
        spinnerSorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myAdapter.sort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //pos = -1;
            }
        });
    }
}

