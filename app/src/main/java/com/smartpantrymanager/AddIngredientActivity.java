
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

public class AddIngredientActivity extends AppCompatActivity {

    EditText etIngredientName, etQuantity, etExpiryDate;
    Spinner spinnerUnit;
    Button btnSaveIngredient;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        databaseHelper = new DatabaseHelper(this);

        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        String[] units = {"kg", "g", "L", "ml", "pieces", "packets"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerUnit.setAdapter(adapter);

        etExpiryDate.setFocusable(false);
        etExpiryDate.setClickable(true);

        etExpiryDate.setOnClickListener(v -> showDatePicker());

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format(
                            java.util.Locale.getDefault(),
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            dayOfMonth
                    );
                    etExpiryDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void saveIngredient() {
        String name = etIngredientName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiryDate = etExpiryDate.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty()) {
            Toast.makeText(this,
                    "Enter the ingredient name and quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Enter a valid quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantity <= 0) {
            Toast.makeText(this,
                    "Quantity must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean saved = databaseHelper.addIngredient(
                name, quantity, unit, expiryDate
        );

        if (saved) {
            Toast.makeText(this,
                    "Ingredient saved successfully!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Failed to save ingredient",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
