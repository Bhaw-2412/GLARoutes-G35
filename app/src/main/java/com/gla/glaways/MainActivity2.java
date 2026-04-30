package com.gla.glaways;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private LinearLayout blockListContainer;
    private EditText searchBar;
    private SharedPreferences sharedPreferences;
    private static final int VOICE_CODE = 100;
    private boolean isFormVisible = false;

    // ✅ APKA DATA
    private final String YOUR_CALLING_NUMBER = "9536268540";
    private final String YOUR_EMAIL_ADDRESS = "manishsingh291100@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);

        // Application start point
        loadHomePage();

        // Handle Back Press for Form and Exit
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isFormVisible) {
                    loadHomePage();
                } else {
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    private void loadHomePage() {
        setContentView(R.layout.activitymain2);
        isFormVisible = false;

        blockListContainer = findViewById(R.id.blockListContainer);
        searchBar = findViewById(R.id.searchBar);

        // ✅ 1. DASHBOARD TRIGGER
        ImageView btnMenu = findViewById(R.id.btnHamburgerMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> showMiniDashboard());
        }

        // ✅ 2. RIDE LOGIC (FREE RIDE LIMIT)
        int rideCount = sharedPreferences.getInt("rideCount", 0);
        Button btnFreeRide = findViewById(R.id.btnCourses);
        if (rideCount >= 10) {
            btnFreeRide.setVisibility(View.GONE);
        }
        btnFreeRide.setOnClickListener(v -> loadRideFormPage());

        // ✅ 3. SEARCH BAR LOGIC (TEXT + VOICE)
        if (searchBar != null) {
            searchBar.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterBlocks(s.toString());
                }
                @Override public void afterTextChanged(Editable s) {}
            });

            searchBar.setOnTouchListener((v, event) -> {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (searchBar.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                        if (event.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            startVoiceSearch();
                            return true;
                        }
                    }
                }
                return false;
            });
        }

        // Initialize all listeners
        setupBlockActionListeners();
    }

    private void setupBlockActionListeners() {
        // ✅ CHIPS (FILTER BUTTONS)
        findViewById(R.id.chipAll).setOnClickListener(v -> { searchBar.setText(""); filterBlocks(""); });
        findViewById(R.id.chipB1).setOnClickListener(v -> searchBar.setText("Block 1"));
        findViewById(R.id.chipB2).setOnClickListener(v -> searchBar.setText("Block 2"));
        findViewById(R.id.chipB3).setOnClickListener(v -> searchBar.setText("Block 3"));
        findViewById(R.id.chipB4).setOnClickListener(v -> searchBar.setText("Block 4"));
        findViewById(R.id.chipB5).setOnClickListener(v -> searchBar.setText("Block 5"));
        findViewById(R.id.chipB6).setOnClickListener(v -> searchBar.setText("Block 6"));
        findViewById(R.id.chipB7).setOnClickListener(v -> searchBar.setText("Block 7"));
        findViewById(R.id.chipB8).setOnClickListener(v -> searchBar.setText("Block 8"));
        findViewById(R.id.chipB9).setOnClickListener(v -> searchBar.setText("Block 9"));
        findViewById(R.id.chipB10).setOnClickListener(v -> searchBar.setText("Block 10"));
        findViewById(R.id.chipB11).setOnClickListener(v -> searchBar.setText("Block 11"));
        findViewById(R.id.chipB12).setOnClickListener(v -> searchBar.setText("Block 12"));

        // ✅ MAP BUTTONS
        findViewById(R.id.btnMap1).setOnClickListener(v -> openMap("Block 1"));
        findViewById(R.id.btnMap2).setOnClickListener(v -> openMap("Block 2"));
        findViewById(R.id.btnMap3).setOnClickListener(v -> openMap("Block 3"));
        findViewById(R.id.btnMap4).setOnClickListener(v -> openMap("Block 4"));
        findViewById(R.id.btnMap5).setOnClickListener(v -> openMap("Block 5"));
        findViewById(R.id.btnMap6).setOnClickListener(v -> openMap("Block 6"));
        findViewById(R.id.btnMap7).setOnClickListener(v -> openMap("Block 7"));
        findViewById(R.id.btnMap8).setOnClickListener(v -> openMap("Block 8"));
        findViewById(R.id.btnMap9).setOnClickListener(v -> openMap("Block 9"));
        findViewById(R.id.btnMap10).setOnClickListener(v -> openMap("Block 10"));
        findViewById(R.id.btnMap11).setOnClickListener(v -> openMap("Block 11"));
        findViewById(R.id.btnMap12).setOnClickListener(v -> openMap("Block 12"));

        // ✅ CALL BUTTONS (SABKE LIYE EK HI LISTENER)
        View.OnClickListener callListener = v -> makeOfficeCall(YOUR_CALLING_NUMBER);
        findViewById(R.id.btnCall1).setOnClickListener(callListener);
        findViewById(R.id.btnCall2).setOnClickListener(callListener);
        findViewById(R.id.btnCall3).setOnClickListener(callListener);
        findViewById(R.id.btnCall4).setOnClickListener(callListener);
        findViewById(R.id.btnCall5).setOnClickListener(callListener);
        findViewById(R.id.btnCall6).setOnClickListener(callListener);
        findViewById(R.id.btnCall7).setOnClickListener(callListener);
        findViewById(R.id.btnCall8).setOnClickListener(callListener);
        findViewById(R.id.btnCall9).setOnClickListener(callListener);
        findViewById(R.id.btnCall10).setOnClickListener(callListener);
        findViewById(R.id.btnCall11).setOnClickListener(callListener);
        findViewById(R.id.btnCall12).setOnClickListener(callListener);

        // ✅ BLOCK DETAILS (CLICK ON CARD)
        String roomMsg = "• Admission: Room-101\n• Fees: Room-102\n• Classroom: Room-103\n• Lab: Room-104\n• Faculty: 001";
        findViewById(R.id.layoutBlock1).setOnClickListener(v -> showRoomDetails("Block 1", roomMsg));
        findViewById(R.id.layoutBlock2).setOnClickListener(v -> showRoomDetails("Block 2", roomMsg));
        findViewById(R.id.layoutBlock3).setOnClickListener(v -> showRoomDetails("Block 3", roomMsg));
        findViewById(R.id.layoutBlock4).setOnClickListener(v -> showRoomDetails("Block 4", roomMsg));
        findViewById(R.id.layoutBlock5).setOnClickListener(v -> showRoomDetails("Block 5", roomMsg));
        findViewById(R.id.layoutBlock6).setOnClickListener(v -> showRoomDetails("Block 6", roomMsg));
        findViewById(R.id.layoutBlock7).setOnClickListener(v -> showRoomDetails("Block 7", roomMsg));
        findViewById(R.id.layoutBlock8).setOnClickListener(v -> showRoomDetails("Block 8", roomMsg));
        findViewById(R.id.layoutBlock9).setOnClickListener(v -> showRoomDetails("Block 9", roomMsg));
        findViewById(R.id.layoutBlock10).setOnClickListener(v -> showRoomDetails("Block 10", roomMsg));
        findViewById(R.id.layoutBlock11).setOnClickListener(v -> showRoomDetails("Block 11", roomMsg));
        findViewById(R.id.layoutBlock12).setOnClickListener(v -> showRoomDetails("Block 12", roomMsg));

        findViewById(R.id.btnHelp).setOnClickListener(v -> startActivity(new Intent(this, MainActivity4.class)));
        findViewById(R.id.btnPlacement).setOnClickListener(v -> {
            startActivity(new Intent(this, PlacementActivity.class));
        });
    }

    private void filterBlocks(String query) {
        String cleanQuery = query.toLowerCase().replaceAll("[\\s.]+", "");

        for (int i = 0; i < blockListContainer.getChildCount(); i++) {
            View child = blockListContainer.getChildAt(i);
            if (query.isEmpty()) {
                child.setVisibility(View.VISIBLE);
                continue;
            }

            // Keyword mapping for all blocks
            String bucket = "";
            switch (i) {
                case 0: bucket = "mcablock1btech"; break;
                case 1: bucket = "electricaleeblock2"; break;
                case 2: bucket = "mechanicalmeblock3"; break;
                case 3: bucket = "biotechblock4"; break;
                case 4: bucket = "llblawblock5"; break;
                case 5: bucket = "agricultureagblock6"; break;
                case 6: bucket = "cseblock7"; break;
                case 7: bucket = "csitblock8"; break;
                case 8: bucket = "diplomablock9"; break;
                case 9: bucket = "mcabcablock10"; break;
                case 10: bucket = "csedblock11"; break;
                case 11: bucket = "mbabbablock12"; break;
            }

            if (bucket.contains(cleanQuery) || ("block" + (i + 1)).contains(cleanQuery)) {
                child.setVisibility(View.VISIBLE);
            } else {
                child.setVisibility(View.GONE);
            }
        }
    }

    private void showMiniDashboard() {
        View dashView = getLayoutInflater().inflate(R.layout.layout_mini_dashboard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setView(dashView);
        final AlertDialog dashDialog = builder.create();
        if (dashDialog.getWindow() != null) dashDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dashView.findViewById(R.id.dashFees).setOnClickListener(v -> { startDetailActivity("COURSE FEES"); dashDialog.dismiss(); });
        dashView.findViewById(R.id.dashEvents).setOnClickListener(v -> { startDetailActivity("EVENTS"); dashDialog.dismiss(); });
        dashView.findViewById(R.id.dashClubs).setOnClickListener(v -> { startDetailActivity("CLUBS"); dashDialog.dismiss(); });
        dashView.findViewById(R.id.dashLibrary).setOnClickListener(v -> { startDetailActivity("LIBRARY"); dashDialog.dismiss(); });
        dashView.findViewById(R.id.btnCloseDash).setOnClickListener(v -> dashDialog.dismiss());
        dashDialog.show();
    }

    private void startDetailActivity(String category) {
        Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
        intent.putExtra("TYPE", category);
        startActivity(intent);
    }

    private void showRoomDetails(String title, String message) {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_custom_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ((TextView)dialogView.findViewById(R.id.dialogTitle)).setText(title);
        ((TextView)dialogView.findViewById(R.id.dialogMessage)).setText(message);
        dialogView.findViewById(R.id.btnOk).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void loadRideFormPage() {
        setContentView(R.layout.activitymain5);
        isFormVisible = true;

        EditText etName = findViewById(R.id.etVisitorName);
        Button btnSave = findViewById(R.id.btnSaveData); // Request Button
        Button btnCall = findViewById(R.id.btnCallToRider);

        // Shuru mein Call button ko disable rakhenge
        btnCall.setEnabled(false);
        btnCall.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Fill Details", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- PURANA RELIABLE METHOD (Intent) ---
            String recipient = "manishsingh291100@gmail.com";
            String subject = "New Ride Request: " + name;
            String body = "Visitor " + name + " has requested a ride on campus.";

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // Sirf email apps trigger hongi
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);

            try {
                // Gmail/Mail app khulega
                startActivity(Intent.createChooser(intent, "Send Request via..."));

                // UI Update: Request bhejte hi call button chalu kar do
                btnCall.setEnabled(true);
                btnCall.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A237E")));

                btnSave.setEnabled(false);
                btnSave.setText("Request Sent ✓");
                btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

                Toast.makeText(this, "Email app open ho rahi hai...", Toast.LENGTH_SHORT).show();

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No email app found!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCall.setOnClickListener(v -> {
            makeOfficeCall(YOUR_CALLING_NUMBER);
            sharedPreferences.edit().putInt("rideCount", sharedPreferences.getInt("rideCount", 0) + 1).apply();
            loadHomePage();
        });
    }

    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try { startActivityForResult(intent, VOICE_CODE); } catch (Exception e) {}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) searchBar.setText(result.get(0));
        }
    }

    private void makeOfficeCall(String number) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
    }

    private void openMap(String block) {
        Intent intent = new Intent(this, MainActivity3.class);
        double lat = 0.0, lng = 0.0;

        // ✅ GLA University ke accurate coordinates jo tune manage kiye hain
        switch (block) {
            case "Block 1": lat = 27.605474; lng = 77.592918; break;
            case "Block 2": lat = 27.606035859815666; lng = 77.59298328477294; break;
            case "Block 3": lat = 27.606138213941573; lng = 77.5931531870174; break;
            case "Block 4": lat = 27.606434; lng = 77.595150; break;
            case "Block 5": lat = 27.605416; lng = 77.595307; break;
            case "Block 6": lat = 27.604633866852303; lng = 77.59579307413186; break;
            case "Block 7": lat = 27.606606636921523; lng = 77.59557301418998; break;
            case "Block 8": lat = 27.606232; lng = 77.595882; break;
            case "Block 9": lat = 27.603303; lng = 77.595334; break;
            case "Block 10": lat = 27.603595607866673; lng = 77.59582331580427; break;
            case "Block 11": lat = 27.603440743945328; lng = 77.59556046699764; break;
            case "Block 12": lat = 27.601829; lng = 77.597343; break;
            default: lat = 27.605474; lng = 77.592918; break; // Default fallback
        }

        intent.putExtra("BLOCK_NAME", block);
        intent.putExtra("LATITUDE", lat);
        intent.putExtra("LONGITUDE", lng);
        startActivity(intent);
    }
}