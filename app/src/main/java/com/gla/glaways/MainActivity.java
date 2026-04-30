package com.gla.glaways;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Elements
        TextView tvGlaMap = findViewById(R.id.tvGlaMap);
        TextView subTitle = findViewById(R.id.subTitle);
        ImageView logoCenter = findViewById(R.id.logo_center);
        MaterialButton btnStart = findViewById(R.id.btnStart);
        TextView btnRegisterMe = findViewById(R.id.btn_register_me);

        // Underline logic for Register Me
        String text = "Register Me";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        btnRegisterMe.setText(content);

        // --- Animations (Same as before) ---
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        tvGlaMap.startAnimation(fadeIn);
        subTitle.startAnimation(fadeIn);

        ScaleAnimation scaleUp = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleUp.setDuration(1200);
        logoCenter.startAnimation(scaleUp);

        // --- CLICK LOGICS WITH LOCK ---

        // 1. Explore Button (btnStart) - Ab ye lock hai!
        btnStart.setOnClickListener(v -> {
            // Check karo ki registration hua hai ya nahi
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isRegistered = sharedPref.getBoolean("isRegistered", false);

            if (isRegistered) {
                // ✅ Agar registered hai, toh maze karo
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                // ❌ Agar nahi hai, toh Alert dikhao
                showRegistrationAlert();
            }
        });

        // 2. Register Link
        btnRegisterMe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    // Alert Dialog Function
    private void showRegistrationAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Registration Required")
                .setMessage("App can't open without registration")
                .setPositiveButton("Register Now", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}