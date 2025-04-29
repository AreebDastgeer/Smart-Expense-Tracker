package com.example.a3_expensetracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<HashMap<String, String>> expensesList;

    OnItemClickListener listener;

    public RecyclerViewAdapter(Activity context, ArrayList<HashMap<String, String>> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int Id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.allexpenseslayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder,int position) {
        HashMap<String, String> expense = expensesList.get(position);
        holder.textID.setText(expense.get("id"));
        holder.textDate.setText(expense.get("date"));
        holder.textCategory.setText(expense.get("category"));
        holder.textAmount.setText(expense.get("amount"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                    HashMap<String, String> clickedExpense = expensesList.get(currentPosition);
                    int actualId = Integer.parseInt(clickedExpense.get("id"));
                    listener.onItemClick(currentPosition, actualId);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return expensesList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textID, textDate, textCategory,textAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textID = itemView.findViewById(R.id.textViewID);
            textDate = itemView.findViewById(R.id.textViewDate);
            textCategory = itemView.findViewById(R.id.textViewCategory);
            textAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }
}

