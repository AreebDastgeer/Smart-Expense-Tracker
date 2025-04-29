package com.example.a3_expensetracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnSortBy;
    Spinner sortSpinner;
    dbQueries dbqueries;
    ArrayList<HashMap<String,String>> expensesList;
    RecyclerViewAdapter adapter;
    ArrayAdapter<CharSequence> sortAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnSortBy=findViewById(R.id.btnSortBy);
        sortSpinner=findViewById(R.id.sortSpinner);

        recyclerView = findViewById(R.id.recyclerviewexpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbqueries = new dbQueries(getApplicationContext());
        expensesList = dbqueries.getAllExpenses();

        adapter = new RecyclerViewAdapter(this,
                expensesList);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int Id) {
                Intent intent = new Intent(MainActivity.this, EditExpenseRecord.class);
                intent.putExtra("id", Id);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);


        sortAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);

        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortSpinner.setAdapter(sortAdapter);

        btnSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sortSpinner.getVisibility() == View.GONE) {
                    sortSpinner.setVisibility(View.VISIBLE);
                } else {
                    sortSpinner.setVisibility(View.GONE);
                }
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();


                switch (selectedOption) {
                    case "Sort by Date":
                        sortByDate();
                        break;
                    case "Sort by Category":
                        sortByCategory();
                        break;
                    case "Sort by Amount":
                        sortByAmount();
                        break;
                }


                sortSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences preferences = getSharedPreferences("MyPrefFile", MODE_PRIVATE);
        checkBudgetAndShowAlert(preferences);


    }

    // Sorting functions
    private void sortByDate() {
        Collections.sort(expensesList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return o1.get("date").compareTo(o2.get("date"));
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByCategory() {
        Collections.sort(expensesList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return o1.get("category").compareTo(o2.get("category"));
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByAmount() {
        Collections.sort(expensesList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return Integer.compare(Integer.parseInt(o1.get("amount")), Integer.parseInt(o2.get("amount"))); // Sort by amount (numeric)
            }
        });
        adapter.notifyDataSetChanged();
    }


    public void AddExpense(View view) {
        Intent intent = new Intent(this, NewExpenseEntry.class);
        startActivity(intent);
    }

    public void SetBudget(View view) {
        Intent intent = new Intent(this, SharedPreference.class);
        startActivity(intent);
    }

    private void checkBudgetAndShowAlert(SharedPreferences preferences) {
        int monthlyBudget = Integer.parseInt(preferences.getString("budgetLimit", "0"));
        int totalExpenses = dbqueries.calculateMonthlyTotalExpenses();

        if (totalExpenses > monthlyBudget) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Budget Exceeded");
            builder.setMessage("Your total monthly expenses (" + totalExpenses + ") exceeded your monthly budget limit (" + monthlyBudget + ")" );
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,
                            "Monthly Budget Exceeded By Rs. " + (totalExpenses - monthlyBudget) , Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Will Do Better Next Time!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}