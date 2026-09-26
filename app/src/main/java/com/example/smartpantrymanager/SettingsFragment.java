package com.example.smartpantrymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch switchExpiry = view.findViewById(R.id.switchExpiry);
        Switch switchUnits = view.findViewById(R.id.switchUnits);
        Button btnClear = view.findViewById(R.id.btnClearData);

        SharedPreferences prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        switchExpiry.setChecked(prefs.getBoolean("expiryAlert", true));
        switchUnits.setChecked(prefs.getBoolean("imperial", false));

        switchExpiry.setOnCheckedChangeListener((b, isChecked) -> {
            prefs.edit().putBoolean("expiryAlert", isChecked).apply();
            Toast.makeText(getContext(), "Alerts: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });

        switchUnits.setOnCheckedChangeListener((b, isChecked) -> {
            prefs.edit().putBoolean("imperial", isChecked).apply();
        });

        btnClear.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(getContext());
            db.clearAllPantry();
            Toast.makeText(getContext(), "Pantry cleared", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}