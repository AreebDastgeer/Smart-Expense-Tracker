package com.example.a3_expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SharedPreference extends AppCompatActivity {
    SharedPreferences preferences;
    EditText edtsharedpref;
    Button button6;
    String budgetLimit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shared_preference);

        edtsharedpref=findViewById(R.id.edtsharedpref);
        button6=findViewById(R.id.button6);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences= getSharedPreferences("MyPrefFile",0);
        Load_Pref();

    }

    public void Save_Pref(View view) {
        SharedPreferences.Editor editor =
                preferences.edit();
        String value= edtsharedpref.getText().toString();
        editor.putString("budgetLimit",value);
        editor.commit();
        Toast.makeText(this, "Preference saved!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Load_Pref() {
        budgetLimit = preferences.getString("budgetLimit", "0");
        edtsharedpref.setText(budgetLimit);
    }

    public void Reset_Pref(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("budgetLimit");
        editor.apply();
        edtsharedpref.setText("");
        Toast.makeText(this, "Preference reset!", Toast.LENGTH_SHORT).show();
    }


}