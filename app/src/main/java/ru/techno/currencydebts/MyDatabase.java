package ru.techno.currencydebts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    final static String TB_NAME = "debts";
    final static String COL_ID = "debt_id";
    final static String COL_NAME = "name";
    final static String COL_SUM = "sum";
    final static String COL_CURRENCY = "currency";
    private final static String DB_NAME = "currency.db";
    private final static int DB_VERSION = 6;

    MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @SuppressLint("LongLogTag")
    static void replace(SQLiteDatabase db, int replaceID, String replaceName, double replaceDebt, String replaceCurrency) {
        db.execSQL("UPDATE " + TB_NAME +
                " SET " + COL_NAME + " = \'" + replaceName + "\', " + COL_SUM + " = " + replaceDebt + ", " + COL_CURRENCY + " = \'" + replaceCurrency + "\'" +
                " WHERE " + COL_ID + " = " + replaceID);
        Log.i("ru.techno.currency/SQL", "UPDATE " + TB_NAME +
                " SET " + COL_NAME + " = \'" + replaceName + "\', " + COL_SUM + " = " + replaceDebt + ", " + COL_CURRENCY + " = \'" + replaceCurrency + "\'" +
                " WHERE " + COL_ID + " = " + replaceID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TB_NAME);
        onCreate(db);
    }

    @SuppressLint("LongLogTag")
    static void put(SQLiteDatabase db, String putName, double putDebt, String putCurrency) {
        db.execSQL("INSERT INTO  " + TB_NAME + " (" + COL_NAME + ", " + COL_SUM + ", " + COL_CURRENCY + ") " +
                "VALUES (\'" + putName + "\', " + putDebt + ", \'" + putCurrency + "\')");
        Log.i("ru.techno.currency/SQL", "INSERT INTO  " + TB_NAME + " (" + COL_NAME + ", " + COL_SUM + ", " + COL_CURRENCY + ") " +
                "VALUES (\'" + putName + "\', " + putDebt + ", \'" + putCurrency + "\')");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR," +
                COL_SUM + " REAL," +
                COL_CURRENCY + " VARCHAR(5))");
    }

    @SuppressLint("LongLogTag")
    public void delete(SQLiteDatabase db, int deleteID){
        db.execSQL("DELETE FROM " + TB_NAME + " WHERE " + COL_ID + " = " + deleteID);
        Log.i("ru.techno.currencydebts/SQL", "DELETE FROM " + TB_NAME + " WHERE " + COL_ID + " = " + deleteID);
    }
}
