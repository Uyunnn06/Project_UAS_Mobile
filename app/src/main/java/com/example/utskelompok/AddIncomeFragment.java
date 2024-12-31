package com.example.utskelompok;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddIncomeFragment extends Fragment {

    private EditText descriptionEditText, amountEditText;
    private Button saveButton;
    private RecyclerView incomeRecyclerView;

    private SQLiteOpenHelper dbHelper; // Deklarasi sebagai variabel kelas

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);

        // Inisialisasi UI
        descriptionEditText = view.findViewById(R.id.incomeDescription);
        amountEditText = view.findViewById(R.id.incomeAmount);
        saveButton = view.findViewById(R.id.saveIncomeButton);
        incomeRecyclerView = view.findViewById(R.id.incomeRecyclerView);

        // Inisialisasi dbHelper sebagai variabel kelas
        dbHelper = new SQLiteOpenHelper(getContext(), "budget_smart.db", null, 4) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS income_table (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "description TEXT, " +
                        "amount INTEGER, " +
                        "time TEXT)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                if (oldVersion < newVersion) {
                    db.execSQL("DROP TABLE IF EXISTS income_table");
                    onCreate(db);
                }
            }
        };

        // Atur RecyclerView
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Muat data setelah dbHelper diinisialisasi
        loadIncomeHistory();

        // Atur aksi tombol simpan
        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString().trim();
            String amountText = amountEditText.getText().toString().trim();

            if (!description.isEmpty() && !amountText.isEmpty()) {
                try {
                    int amount = Integer.parseInt(amountText);
                    addIncomeToDatabase(description, amount);
                    Toast.makeText(getActivity(), "Pemasukan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();

                    descriptionEditText.setText("");
                    amountEditText.setText("");

                    loadIncomeHistory();

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Jumlah harus berupa angka!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addIncomeToDatabase(String description, int amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("amount", amount);
        values.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

        db.insert("income_table", null, values);
    }

    private void loadIncomeHistory() {
        if (dbHelper == null) {
            Toast.makeText(getContext(), "Database belum diinisialisasi.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM income_table ORDER BY time DESC", null);

        IncomeAdapter adapter = new IncomeAdapter(cursor);
        incomeRecyclerView.setAdapter(adapter);
    }
}