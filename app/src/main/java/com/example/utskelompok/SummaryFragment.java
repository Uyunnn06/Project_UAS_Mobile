package com.example.utskelompok;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class SummaryFragment extends Fragment {

    private TextView totalIncomeTextView, totalExpenseTextView, remainingBalanceTextView;
    private RecyclerView recentTransactionsRecyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;
    private String formatCurrency(int amount) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        return "Rp " + numberFormat.format(amount);
    }

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        totalIncomeTextView = view.findViewById(R.id.totalIncome);
        totalExpenseTextView = view.findViewById(R.id.totalExpense);
        remainingBalanceTextView = view.findViewById(R.id.remainingBalance);
        recentTransactionsRecyclerView = view.findViewById(R.id.recentTransactionsRecyclerView);

        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList);

        recentTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentTransactionsRecyclerView.setAdapter(transactionAdapter);

        loadSummary(); // Memanggil loadSummary saat pertama kali fragment dibuat

        return view;
    }

    public void loadSummary() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BudgetSmart", Context.MODE_PRIVATE);
        String incomesJson = sharedPreferences.getString("incomes", "[]");
        String expensesJson = sharedPreferences.getString("expenses", "[]");

        try {
            JSONArray incomesArray = new JSONArray(incomesJson);
            JSONArray expensesArray = new JSONArray(expensesJson);

            int totalIncome = 0;
            int totalExpense = 0;

            // Parse incomes
            for (int i = 0; i < incomesArray.length(); i++) {
                JSONObject incomeObject = incomesArray.getJSONObject(i);
                String description = incomeObject.getString("description");
                int amount = incomeObject.getInt("amount");
                String date = incomeObject.getString("time");

                totalIncome += amount;
                transactionList.add(new Transaction(description, amount, date, "income"));
            }

            // Parse expenses
            for (int i = 0; i < expensesArray.length(); i++) {
                JSONObject expenseObject = expensesArray.getJSONObject(i);
                String description = expenseObject.getString("description");
                int amount = expenseObject.getInt("amount");
                String date = expenseObject.getString("time");

                totalExpense += amount;
                transactionList.add(new Transaction(description, amount, date, "expense"));
            }

            // Update UI
            totalIncomeTextView.setText(formatCurrency(totalIncome));
            totalExpenseTextView.setText(formatCurrency(totalExpense));
            int remainingBalance = totalIncome - totalExpense;
            remainingBalanceTextView.setText(formatCurrency(remainingBalance));

            // Refresh transaction list
            transactionAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}