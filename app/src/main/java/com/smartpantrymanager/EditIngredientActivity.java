
package com.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiryDate;
    private Spinner spinnerUnit;
    private DatabaseHelper databaseHelper;
    private int ingredientId;

    private final String[] units = {
            "kg", "g", "L", "ml", "pieces", "packets"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        databaseHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etEditIngredientName);
        etQuantity = findViewById(R.id.etEditQuantity);
        etExpiryDate = findViewById(R.id.etEditExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerEditUnit);
        Button btnUpdate = findViewById(R.id.btnUpdateIngredient);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerUnit.setAdapter(adapter);

        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        etName.setText(getIntent().getStringExtra("ingredient_name"));
        etQuantity.setText(getIntent().getStringExtra("ingredient_quantity"));
        etExpiryDate.setText(getIntent().getStringExtra("ingredient_expiry"));

        String savedUnit = getIntent().getStringExtra("ingredient_unit");
        if (savedUnit != null) {
            for (int i = 0; i < units.length; i++) {
                if (units[i].equals(savedUnit)) {
                    spinnerUnit.setSelection(i);
                    break;
                }
            }
        }

        etExpiryDate.setOnClickListener(v -> showDatePicker());

        btnUpdate.setOnClickListener(v -> updateIngredient());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String date = String.format(
                            java.util.Locale.getDefault(),
                            "%04d-%02d-%02d",
                            year, month + 1, day
                    );
                    etExpiryDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void updateIngredient() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();

        if (ingredientId == -1) {
            Toast.makeText(this, "Ingredient not found",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (name.isEmpty() || quantityText.isEmpty()) {
            Toast.makeText(this, "Enter a name and quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantity <= 0) {
            Toast.makeText(this, "Quantity must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = databaseHelper.updateIngredient(
                ingredientId, name, quantity, unit, expiryDate
        );

        if (updated) {
            Toast.makeText(this, "Ingredient updated successfully!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Could not update ingredient",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
