package com.example.utskelompok;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNav);

        // Set default fragment to SummaryFragment
        if (savedInstanceState == null) {
            switchFragment(new SummaryFragment());
        }

        // Handle navigation item selection using if-else logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment;

            if (itemId == R.id.nav_summary) {
                selectedFragment = new SummaryFragment();
            } else if (itemId == R.id.nav_expenses) {
                selectedFragment = new AddExpenseFragment();
            } else if (itemId == R.id.nav_incomes) {
                selectedFragment = new AddIncomeFragment();
            } else {
                return false; // No valid fragment found
            }

            // Switch to the selected fragment
            switchFragment(selectedFragment);
            return true;
        });
    }

    /**
     * Replaces the current fragment with the selected fragment.
     *
     * @param fragment Fragment to display.
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}