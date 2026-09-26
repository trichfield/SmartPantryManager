package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        FloatingActionButton fab = findViewById(R.id.fab);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PantryFragment()).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();
            if (id == R.id.navigation_pantry) {
                selected = new PantryFragment();
            } else if (id == R.id.navigation_recipes) {
                selected = new RecipesFragment();
            } else if (id == R.id.navigation_suggested) {
                startActivity(new Intent(MainActivity.this, SuggestedActivity.class));
                return true;
            } else if (id == R.id.navigation_settings) {
                selected = new SettingsFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                return true;
            }
            return false;
        });

        fab.setOnClickListener(v -> {
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (current instanceof PantryFragment || current instanceof RecipesFragment) {
                startActivity(new Intent(this, AddEditActivity.class));
            }
        });
    }
}