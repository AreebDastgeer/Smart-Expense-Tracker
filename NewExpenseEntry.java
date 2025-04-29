package com.example.a3_expensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class NewExpenseEntry extends AppCompatActivity {
    EditText amount, category, date;
    Button btnsave;
    dbQueries dbqueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_expense_entry);

        amount = findViewById(R.id.edtamount);
        category = findViewById(R.id.edtcategory);
        date = findViewById(R.id.edtdate);
        btnsave = findViewById(R.id.btnsave);

        date.setFocusable(false);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewExpenseEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                Calendar chosenDate = Calendar.getInstance();
                                chosenDate.set(year, monthOfYear, dayOfMonth);


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String formattedDate = sdf.format(chosenDate.getTime());


                                date.setText(formattedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void SaveIntoDatabase(View view) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("amount", amount.getText().toString());
        hashMap.put("category", category.getText().toString());
        hashMap.put("date", date.getText().toString());
        dbqueries = new dbQueries(getApplicationContext());
        dbqueries.Insert(hashMap);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

