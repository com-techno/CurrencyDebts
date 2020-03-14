package ru.techno.currencydebts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DebtActivity extends Activity {
    //For spinnerCurrency
    static String[] sCurrencySymbols = {"\u20BD", "€", "$", "₤", "¥"};

    EditText ETName, ETDebt;
    Spinner spinnerCurrency;
    RadioGroup radioGroup;

    //From recycler for editing
    String sName;
    double dDebt;

    boolean bAction = false; //Edit(true) or create(false)
    int iID; //Debt ID in Database
    int iPos = 0; //Selected position in spinnerCurrency

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        getFromArguments();
        bindAdapterToSpinnerCurrency();
    }

    @SuppressLint({"LongLogTag", "ResourceType"})
    public void onAddClick(View v) {
        sName = ETName.getText().toString();
        Log.i("lol", String.valueOf(radioGroup.getCheckedRadioButtonId()));
        if (!sName.equals("") && !ETDebt.getText().toString().equals("") && iPos != -1) {
            dDebt = Double.parseDouble(ETDebt.getText().toString());
            SQLiteDatabase db = MainActivity.myDatabase.getWritableDatabase();
            if (bAction) {
                MyDatabase.replace(db, iID, sName, dDebt, sCurrencySymbols[iPos]);
                Log.i("ru.techno.currencydebts/Database", "edited with id = " + iID);
            } else {
                MyDatabase.put(db, sName, dDebt, sCurrencySymbols[iPos]);
                Log.i("ru.techno.currencydebts/Database", "added");
            }

            MainActivity.myAdapter.update();
            finish();
        } else Toast.makeText(this, "Заполните поля!", Toast.LENGTH_LONG).show();
    }

    public void onRadioClick(View v){
        switch (v.getId()){
            case R.id.rbme: ETName.setHint("Кто должен?"); break;
            case R.id.rbi: ETName.setHint("Кому должен?"); break;
        }
    }

    private void bindViews(){
        setContentView(R.layout.add);
        ETName = findViewById(R.id.addName);
        ETDebt = findViewById(R.id.addDebt);
        spinnerCurrency = findViewById(R.id.currencySpinner);
        radioGroup = findViewById(R.id.rg);
    }

    private void getFromArguments(){
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        bAction = arguments.getBoolean("flag");
        if (bAction) {
            iID = arguments.getInt("id");
            sName = arguments.getString("name");
            dDebt = arguments.getDouble("debt");
            spinnerCurrency.setId(arguments.getInt("currency"));
            ETName.setText(sName);
            ETDebt.setText(dDebt + "");
        }
    }

    private void bindAdapterToSpinnerCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.curr, R.id.currencyText, sCurrencySymbols);
        adapter.setDropDownViewResource(R.layout.curr);
        spinnerCurrency.setAdapter(adapter);
        spinnerCurrency.setPrompt("Валюта");
        spinnerCurrency.setSelection(0);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //pos = -1;
            }
        });
    }
}
