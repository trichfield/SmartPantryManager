package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    EditText etName, etQty, etUnit, etExpiry;
    Button btnSave;
    DatabaseHelper dbHelper;
    int editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        etName = findViewById(R.id.etName);
        etQty = findViewById(R.id.etQty);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("id")) {
            editId = getIntent().getIntExtra("id", -1);
            etName.setText(getIntent().getStringExtra("name"));
            etQty.setText(String.valueOf(getIntent().getDoubleExtra("qty", 0)));
            etUnit.setText(getIntent().getStringExtra("unit"));
            etExpiry.setText(getIntent().getStringExtra("expiry"));
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String qtyStr = etQty.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();

            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Name and quantity required", Toast.LENGTH_SHORT).show();
                return;
            }

            double qty = Double.parseDouble(qtyStr);
            if (qty <= 0) {
                etQty.setError("Quantity must be > 0");
                return;
            }

            if (editId == -1) {
                PantryItem item = new PantryItem(name, qty, unit, expiry);
                dbHelper.addPantryItem(item);
            } else {
                PantryItem item = new PantryItem(name, qty, unit, expiry);
                item.setId(editId);
                dbHelper.updatePantryItem(item);
            }

            finish();
        });
    }
}