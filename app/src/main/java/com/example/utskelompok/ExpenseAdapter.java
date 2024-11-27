package com.example.utskelompok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private JSONArray expenses;
    private String formatCurrency(int amount) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        return "Rp " + numberFormat.format(amount);
    }

    public ExpenseAdapter(JSONArray expenses) {
        this.expenses = expenses;
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
        try {
            JSONObject expense = expenses.getJSONObject(position);
            holder.descriptionTextView.setText(expense.getString("description"));

            // Format amount
            int amount = expense.getInt("amount");
            holder.amountTextView.setText(formatCurrency(amount));

            holder.timeTextView.setText(expense.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return expenses.length();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, amountTextView, timeTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.expenseDescription);
            amountTextView = itemView.findViewById(R.id.expenseAmount);
            timeTextView = itemView.findViewById(R.id.expenseTime);
        }
    }
}