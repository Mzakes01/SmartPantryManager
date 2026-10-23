package com.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etUserEmail;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        Button btnSaveSettings = findViewById(R.id.btnSaveSettings);

        preferences = getSharedPreferences(
                "SmartPantrySettings",
                MODE_PRIVATE
        );

        etUserName.setText(
                preferences.getString("user_name", "")
        );

        etUserEmail.setText(
                preferences.getString("user_email", "")
        );

        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        String name = etUserName.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();

        if (name.isEmpty()) {
            etUserName.setError("Please enter your name");
            etUserName.requestFocus();
            return;
        }

        if (email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {
            etUserEmail.setError("Enter a valid email address");
            etUserEmail.requestFocus();
            return;
        }

        preferences.edit()
                .putString("user_name", name)
                .putString("user_email", email)
                .apply();

        Toast.makeText(
                this,
                "Settings saved successfully!",
                Toast.LENGTH_SHORT
        ).show();
    }
}
