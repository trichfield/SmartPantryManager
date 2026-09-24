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

            if (name.isEmpty()) {
                etName.setError("Required");
                return;
            }
            if (qtyStr.isEmpty()) {
                etQty.setError("Required");
                return;
            }
            double qty = Double.parseDouble(qtyStr);
            if (qty <= 0) {
                etQty.setError("Quantity must be > 0");
                return;
            }

            if (editId == -1) {
                dbHelper.addPantryItem(name, qty, unit, expiry);
            } else {
                dbHelper.updatePantryItem(editId, name, qty, unit, expiry);
            }
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}