package com.example.utskelompok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final ArrayList<AddExpenseFragment.ExpenseItem> expenseList;

    private String formatAmount(int amount) {
        return String.valueOf(amount);
    }

    public ExpenseAdapter(ArrayList<AddExpenseFragment.ExpenseItem> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        AddExpenseFragment.ExpenseItem expense = expenseList.get(position);
        holder.descriptionTextView.setText(expense.description);
        holder.amountTextView.setText(formatAmount(expense.amount));
        holder.timeTextView.setText(expense.time);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, amountTextView, timeTextView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}