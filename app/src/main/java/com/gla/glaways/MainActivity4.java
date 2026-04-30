package com.gla.glaways;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity4 extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout activeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain4);

        // --- SETUP ONLY 360 BUTTONS (Images are NOT clickable anymore) ---
        for (int i = 1; i <= 12; i++) {
            // Logic: Odd blocks = b1, Even blocks = b2
            String imageName = (i % 2 == 0) ? "b2" : "b1";

            // 360 Panorama Action Button Setup
            int btnId = getResources().getIdentifier("btn360_B" + i, "id", getPackageName());
            if (btnId != 0) {
                setupPanoramaAction(btnId, imageName);
            }

            // Image setup (Sirf image set karne ke liye, click ke liye nahi)
            int imgId = getResources().getIdentifier("imgB" + i, "id", getPackageName());
            ImageView iv = findViewById(imgId);
            if (iv != null) {
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                iv.setImageResource(resId);
                iv.setClickable(false); // Image click block kar diya
            }
        }

        // Setup Add Photo Actions (Keep these if you want to add user photos)

    }

    // Function to open 360 Panorama View
    private void setupPanoramaAction(int btnId, String imageName) {
        View btn = findViewById(btnId);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                Intent intent = new Intent(this, PanoramaACtivity.class);
                intent.putExtra("IMAGE_NAME", imageName);
                startActivity(intent);
            });
        }
    }

    // --- Baki functions (Add Photo, dpToPx, onActivityResult) waise hi rahenge ---
    private void setupAddAction(int cardId, int containerId) {
        View addCard = findViewById(cardId);
        LinearLayout container = findViewById(containerId);
        if (addCard != null && container != null) {
            addCard.setOnClickListener(v -> {
                activeContainer = container;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (activeContainer != null) {
                        addNewImageToGallery(imageUri, activeContainer);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Permission Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void addNewImageToGallery(Uri uri, LinearLayout container) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(280), dpToPx(180));
        params.setMargins(0, 0, dpToPx(15), 0);
        card.setLayoutParams(params);
        card.setRadius(dpToPx(20));
        card.setCardElevation(dpToPx(5));

        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setImageURI(uri);

        // Sirf manually added image clickable hogi view karne ke liye
        iv.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullImageActivity.class);
            intent.putExtra("image_uri", uri.toString());
            startActivity(intent);
        });

        card.addView(iv);
        int count = container.getChildCount();
        container.addView(card, count > 0 ? count - 1 : 0);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}