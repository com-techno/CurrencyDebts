package ru.techno.currencydebts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private MyDatabase myDatabase;
    private ArrayList<Information> ALInformation = new ArrayList<>();

    private static class Information{
        String sName;
        double dDebt;
        int iID;
        String sCurrency;

        public Information(String sName, Double dDebt, Integer iID, String sCurrency) {
            this.sName = sName;
            this.dDebt = dDebt;
            this.iID = iID;
            this.sCurrency = sCurrency;
        }
    }

    MyAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        myDatabase = new MyDatabase(context);
    }

    void update() {
        ALInformation.clear();
        readDatabase();
    }

    void sort(final int iSortBy){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ALInformation.sort(new Comparator<Information>() {
                @Override
                public int compare(Information o1, Information o2) {
                    switch (iSortBy) {
                        case 0: return o1.sName.compareToIgnoreCase(o2.sName);
                        case 1: return o2.sName.compareToIgnoreCase(o1.sName);
                        case 2: return Double.compare(o1.dDebt, o2.dDebt);
                        case 3: return Double.compare(o2.dDebt, o1.dDebt);
                        default: return Integer.compare(o1.iID, o2.iID);
                    }
                }
            });
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_debt, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Information info = ALInformation.get(position);
        holder.TWName.setText(info.sName);
        holder.TWDebt.setText(info.sCurrency + info.dDebt);
        holder.TWID.setText(String.valueOf(info.iID));
    }

    @Override
    public int getItemCount() {
        return ALInformation.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TWName;
        TextView TWDebt;
        RelativeLayout RLItem;
        TextView TWID;
        Button BEdit;
        Button BDelete;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            bindViews();
            BDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDatabase.delete(myDatabase.getWritableDatabase(), Integer.parseInt(TWID.getText().toString()));
                    MainActivity.myAdapter.update();
                }
            });
            BEdit.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View v) {
                    Log.i("ru.techno.currencydebts/ItemClick", "id = " + TWID.getText().toString());
                    Intent intent = new Intent(v.getContext(), DebtActivity.class);
                    String[] columns = {MyDatabase.COL_NAME, MyDatabase.COL_SUM, MyDatabase.COL_CURRENCY};
                    @SuppressLint("Recycle") Cursor cursor = myDatabase
                            .getReadableDatabase()
                            .query(MyDatabase.TB_NAME, columns,
                                    MyDatabase.COL_ID + " = " + TWID.getText().toString(),
                                    null, null, null, null);
                    String fromDBsName = null;
                    double fromDBdDebt = 0;
                    int fromDBiCurrency = 0;
                    if (cursor.moveToFirst())
                        do {
                            fromDBsName = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_NAME));
                            fromDBdDebt = cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_SUM));
                            fromDBiCurrency = cursor.getInt(cursor.getColumnIndex(MyDatabase.COL_CURRENCY));
                        } while (cursor.moveToNext());
                    cursor.close();
                    putExtraToIntent(intent, fromDBsName, fromDBdDebt, fromDBiCurrency);
                    v.getContext().startActivity(intent);
                }
            });
        }

        private void bindViews() {
            BEdit = itemView.findViewById(R.id.edit_button);
            RLItem = itemView.findViewById(R.id.rel);
            TWName = itemView.findViewById(R.id.name);
            TWDebt = itemView.findViewById(R.id.debt);
            TWID = itemView.findViewById(R.id.debt_id);
            BDelete = itemView.findViewById(R.id.delete_button);
        }

        private void putExtraToIntent(Intent intent, String fromDBName, double fromDBDebt, int fromDBCurrency) {
            intent.putExtra("flag", true);
            intent.putExtra("id", Integer.parseInt(TWID.getText().toString()));
            intent.putExtra("name", fromDBName);
            intent.putExtra("debt", fromDBDebt);
            intent.putExtra("currency", fromDBCurrency);
        }
    }

    private void readDatabase(){
        SQLiteDatabase database = myDatabase.getReadableDatabase();
        Cursor cursor = database.query(MyDatabase.TB_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                ALInformation.add(new Information(
                        cursor.getString(cursor.getColumnIndex(MyDatabase.COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_SUM)),
                        cursor.getInt(cursor.getColumnIndex(MyDatabase.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(MyDatabase.COL_CURRENCY))));
            } while (cursor.moveToNext());

        cursor.close();
        notifyDataSetChanged();
    }
}

