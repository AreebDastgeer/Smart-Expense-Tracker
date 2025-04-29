package com.example.a3_expensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditExpenseRecord extends AppCompatActivity {
    EditText editDate, editCategory, editAmount;
    Button btnUpdate, btnDelete;
    dbQueries dbqueries;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_expense_record);
        editDate = findViewById(R.id.editdate);
        editCategory = findViewById(R.id.editcategory);
        editAmount = findViewById(R.id.editamount);
        btnUpdate = findViewById(R.id.btnupdate);
        btnDelete = findViewById(R.id.btndelete);

        editDate.setFocusable(false);


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditExpenseRecord.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                Calendar chosenDate = Calendar.getInstance();
                                chosenDate.set(year, monthOfYear, dayOfMonth);


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String formattedDate = sdf.format(chosenDate.getTime());


                                editDate.setText(formattedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });

        dbqueries= new dbQueries(getApplicationContext());
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        HashMap<String,String> hashMap = dbqueries.getSingleRecord(id);
        if (hashMap.size() != 0)
        {
            editDate.setText(hashMap.get("date"));
            editCategory.setText(hashMap.get("category"));
            editAmount.setText(hashMap.get("amount"));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void UpdateContact(View view) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("date",editDate.getText().toString());
        hashMap.put("category",editCategory.getText().toString());
        hashMap.put("amount",editAmount.getText().toString());

        dbqueries.Update(id,hashMap);
        Toast.makeText(this,
                "Record Updated In Database", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,
                MainActivity.class);
        startActivity(intent);
    }

    public void DeleteContact(View view) {
        dbqueries.Delete(id);
        Toast.makeText(this,
                "Record Deleted From Database", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,
                MainActivity.class);
        startActivity(intent);
    }
}