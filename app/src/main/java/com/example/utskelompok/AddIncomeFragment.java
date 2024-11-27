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

public class AddIncomeFragment extends Fragment {

    private EditText descriptionEditText, amountEditText;
    private Button saveButton;
    private RecyclerView incomeRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);

        descriptionEditText = view.findViewById(R.id.incomeDescription);
        amountEditText = view.findViewById(R.id.incomeAmount);
        saveButton = view.findViewById(R.id.saveIncomeButton);
        incomeRecyclerView = view.findViewById(R.id.incomeRecyclerView);

        // Setup RecyclerView
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadIncomeHistory();

        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString();
            String amountText = amountEditText.getText().toString();

            if (!description.isEmpty() && !amountText.isEmpty()) {
                try {
                    int amount = Integer.parseInt(amountText);
                    addIncomeToSharedPreferences(description, amount);
                    Toast.makeText(getActivity(), "Pemasukan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();

                    // Clear input fields
                    descriptionEditText.setText("");
                    amountEditText.setText("");

                    // Reload data
                    loadIncomeHistory();

                    // Memperbarui ringkasan setelah menambahkan pemasukan
                    if (getActivity() != null) {
                        SummaryFragment summaryFragment = (SummaryFragment) getActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag(SummaryFragment.class.getSimpleName());

                        if (summaryFragment != null) {
                            summaryFragment.loadSummary(); // Memanggil ulang loadSummary untuk memperbarui ringkasan
                        }
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Jumlah harus berupa angka!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addIncomeToSharedPreferences(String description, int amount) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BudgetSmart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String existingIncomes = sharedPreferences.getString("incomes", "[]");
        try {
            JSONArray incomesArray = new JSONArray(existingIncomes);

            JSONObject incomeObject = new JSONObject();
            incomeObject.put("description", description);
            incomeObject.put("amount", amount);

            // Add current time
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            incomeObject.put("time", currentTime);

            incomesArray.put(incomeObject);
            editor.putString("incomes", incomesArray.toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadIncomeHistory() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BudgetSmart", Context.MODE_PRIVATE);
        String existingIncomes = sharedPreferences.getString("incomes", "[]");

        try {
            JSONArray incomesArray = new JSONArray(existingIncomes);
            IncomeAdapter adapter = new IncomeAdapter(incomesArray);
            incomeRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}