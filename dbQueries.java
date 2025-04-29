package com.example.a3_expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class dbQueries extends SQLiteOpenHelper {

    public dbQueries (Context context){
        super(context, "ExpenseTrackerDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query = "CREATE TABLE Expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "category TEXT, " +
                " amount FLOAT)" ;
        db.execSQL(Query);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void Insert(HashMap<String, String> hashMap){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues expense = new ContentValues();
        expense.put("date", hashMap.get("date"));
        expense.put("category", hashMap.get("category"));
        expense.put("amount", hashMap.get("amount"));
        db.insert("Expenses", null, expense);

    }

    public ArrayList<HashMap<String,String>> getAllExpenses(){
        ArrayList<HashMap<String,String>> expensesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM Expenses";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> expense = new HashMap<>();
                expense.put("id", cursor.getString(0));
                expense.put("date", cursor.getString(1));
                expense.put("category", cursor.getString(2));
                expense.put("amount", cursor.getString(3));
                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        return expensesList;
    }

    public HashMap<String,String> getExpenseById(int id){
        HashMap<String,String> expense = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM Expenses WHERE id = " + id;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            expense.put("id", cursor.getString(0));
            expense.put("date", cursor.getString(1));
            expense.put("category", cursor.getString(2));
            expense.put("amount", cursor.getString(3));

        }
        return expense;
    }

    public void Delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Expenses", "id = " + id, null);
    }

    public void Update(int id, HashMap<String,String> hashMap){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues expense = new ContentValues();
        expense.put("date", hashMap.get("date"));
        expense.put("category", hashMap.get("category"));
        expense.put("amount", hashMap.get("amount"));
        db.update("Expenses",expense,"id = " + id,null);
    }

    public HashMap<String, String> getSingleRecord(int id) {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String,String> hashMap = new HashMap<>();
        String Query = "SELECT * FROM Expenses WHERE id="+ id;
        Cursor cursor = db.rawQuery(Query,null);
        if (cursor.moveToFirst())
        {
            hashMap.put("id",cursor.getString(0));
            hashMap.put("date", cursor.getString(1));
            hashMap.put("category", cursor.getString(2));
            hashMap.put("amount", cursor.getString(3));
        }
        return hashMap;

    }

    public int calculateMonthlyTotalExpenses() {
        int totalExpenses = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT SUM(amount) " +
                "FROM Expenses " +
                "WHERE strftime('%m', date) = strftime('%m', 'now')";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getInt(0);
        }
        return totalExpenses;

    }
}
