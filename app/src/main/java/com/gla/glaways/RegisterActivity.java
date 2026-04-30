package com.gla.glaways;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameField, emailField, phoneField;
    private MaterialButton btnRegister;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityregister); // Make sure XML name is correct

        // Firebase Realtime Database Initialize
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Mapping IDs from XML
        nameField = findViewById(R.id.reg_name);
        emailField = findViewById(R.id.reg_email);
        phoneField = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister); // XML mein ID btnRegister rakhi hai

        btnRegister.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();

        // Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() != 10) {
            Toast.makeText(this, "Enter a valid 10-digit number", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- STEP 1: Save Data to Firebase Database (Optional but good for records) ---
        // Hum phone number ko hi unique key bana lete hain (Kyunki OTP Auth hata diya hai)
        String userId = phone;

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("phone", phone);

        mDatabase.child(userId).setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                // --- STEP 2: One-Time Registration Lock (SharedPrefs) ---
                SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isRegistered", true); // Yahi wo lock hai
                editor.putString("userName", name);
                editor.putString("userPhone", phone);
                editor.apply();

                Toast.makeText(this, "Welcome to Gla Routes!", Toast.LENGTH_SHORT).show();

                // --- STEP 3: Navigate to Main Dashboard ---
                // Yahan MainActivity par bhej rahe hain jahan se Explore khulta hai
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Database Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}