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

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private JSONArray incomes;
    private String formatCurrency(int amount) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        return "Rp " + numberFormat.format(amount);
    }

    public IncomeAdapter(JSONArray incomes) {
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        try {
            JSONObject income = incomes.getJSONObject(position);
            holder.descriptionTextView.setText(income.getString("description"));

            // Format amount
            int amount = income.getInt("amount");
            holder.amountTextView.setText(formatCurrency(amount));

            holder.timeTextView.setText(income.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return incomes.length();
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, amountTextView, timeTextView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.incomeDescription);
            amountTextView = itemView.findViewById(R.id.incomeAmount);
            timeTextView = itemView.findViewById(R.id.incomeTime);
        }
    }
}