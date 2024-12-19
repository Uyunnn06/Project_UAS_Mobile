package com.example.utskelompok;

import android.content.ContentValues;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {

    private EditText descriptionEditText, amountEditText;
    private Button saveButton;
    private RecyclerView expenseRecyclerView;

    private SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        descriptionEditText = view.findViewById(R.id.expenseDescription);
        amountEditText = view.findViewById(R.id.expenseAmount);
        saveButton = view.findViewById(R.id.saveExpenseButton);
        expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView);

        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(getContext(), "budget_smart.db", null, 3) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String CREATE_TABLE_EXPENSE = "CREATE TABLE IF NOT EXISTS expense_table (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "description TEXT, " +
                        "amount INTEGER, " +
                        "time TEXT)";
                db.execSQL(CREATE_TABLE_EXPENSE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                if (oldVersion < newVersion) {
                    db.execSQL("DROP TABLE IF EXISTS expense_table");
                    onCreate(db);
                }
            }
        };

        database = dbHelper.getWritableDatabase();

        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadExpenseHistory();

        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString().trim();
            String amountText = amountEditText.getText().toString().trim();

            if (!description.isEmpty() && !amountText.isEmpty()) {
                try {
                    int amount = Integer.parseInt(amountText);
                    addExpenseToDatabase(description, amount);
                    Toast.makeText(getActivity(), "Pengeluaran berhasil ditambahkan!", Toast.LENGTH_SHORT).show();

                    descriptionEditText.setText("");
                    amountEditText.setText("");

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

    private void addExpenseToDatabase(String description, int amount) {
        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("amount", amount);

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put("time", currentTime);

        database.insert("expense_table", null, values);
    }

    private void loadExpenseHistory() {
        ArrayList<ExpenseItem> expenseList = new ArrayList<>();
        Cursor cursor = database.query("expense_table", null, null, null, null, null, "time DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                expenseList.add(new ExpenseItem(description, amount, time));
            }
            cursor.close();
        }

        ExpenseAdapter adapter = new ExpenseAdapter(expenseList);
        expenseRecyclerView.setAdapter(adapter);
    }

    public static class ExpenseItem {
        String description;
        int amount;
        String time;

        public ExpenseItem(String description, int amount, String time) {
            this.description = description;
            this.amount = amount;
            this.time = time;
        }
    }

    public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

        private final ArrayList<ExpenseItem> expenseList;

        public ExpenseAdapter(ArrayList<ExpenseItem> expenseList) {
            this.expenseList = expenseList;
        }

        @Override
        public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
            return new ExpenseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ExpenseViewHolder holder, int position) {
            ExpenseItem expense = expenseList.get(position);
            holder.descriptionTextView.setText(expense.description);
            holder.amountTextView.setText("Rp " + expense.amount);
            holder.timeTextView.setText(expense.time);
        }

        @Override
        public int getItemCount() {
            return expenseList.size();
        }

        public class ExpenseViewHolder extends RecyclerView.ViewHolder {
            TextView descriptionTextView, amountTextView, timeTextView;

            public ExpenseViewHolder(View itemView) {
                super(itemView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                amountTextView = itemView.findViewById(R.id.amountTextView);
                timeTextView = itemView.findViewById(R.id.timeTextView);
            }
        }
    }
}