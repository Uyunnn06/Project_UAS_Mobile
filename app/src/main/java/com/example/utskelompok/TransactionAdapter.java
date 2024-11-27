package com.example.utskelompok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.descriptionTextView.setText(transaction.getDescription());
        holder.amountTextView.setText(formatCurrency(transaction.getAmount()));
        holder.dateTextView.setText(transaction.getDate());

        // Color based on transaction type
        if (transaction.getType().equals("income")) {
            holder.amountTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.amountTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    private String formatCurrency(int amount) {
        // Format as Indonesian Rupiah
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, amountTextView, dateTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.transactionDescription);
            amountTextView = itemView.findViewById(R.id.transactionAmount);
            dateTextView = itemView.findViewById(R.id.transactionDate);
        }
    }
}