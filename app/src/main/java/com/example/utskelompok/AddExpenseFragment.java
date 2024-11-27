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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {

    private EditText descriptionEditText, amountEditText;
    private Button saveButton;
    private RecyclerView expenseRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        descriptionEditText = view.findViewById(R.id.expenseDescription);
        amountEditText = view.findViewById(R.id.expenseAmount);
        saveButton = view.findViewById(R.id.saveExpenseButton);
        expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView);

        // Setup RecyclerView
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadExpenseHistory();

        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString();
            String amountText = amountEditText.getText().toString();

            if (!description.isEmpty() && !amountText.isEmpty()) {
                try {
                    int amount = Integer.parseInt(amountText);
                    addExpenseToSharedPreferences(description, amount);
                    Toast.makeText(getActivity(), "Pengeluaran berhasil ditambahkan!", Toast.LENGTH_SHORT).show();

                    // Clear input fields
                    descriptionEditText.setText("");
                    amountEditText.setText("");

                    // Reload data
                    loadExpenseHistory();

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Jumlah harus berupa angka!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addExpenseToSharedPreferences(String description, int amount) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BudgetSmart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String existingExpenses = sharedPreferences.getString("expenses", "[]");
        try {
            JSONArray expensesArray = new JSONArray(existingExpenses);

            JSONObject expenseObject = new JSONObject();
            expenseObject.put("description", description);
            expenseObject.put("amount", amount);

            // Add current time
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            expenseObject.put("time", currentTime);

            expensesArray.put(expenseObject);
            editor.putString("expenses", expensesArray.toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadExpenseHistory() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BudgetSmart", Context.MODE_PRIVATE);
        String existingExpenses = sharedPreferences.getString("expenses", "[]");

        try {
            JSONArray expensesArray = new JSONArray(existingExpenses);
            ExpenseAdapter adapter = new ExpenseAdapter(expensesArray);
            expenseRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}