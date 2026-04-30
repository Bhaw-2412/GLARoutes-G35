package com.gla.glaways;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.chrisbanes.photoview.PhotoView;
// DHYAN RAKHNA: Yahan 'import android.R;' nahi hona chahiye!

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image);

        // Ab ye error nahi dega
        PhotoView photoView = findViewById(R.id.photoView);
        ImageButton btnClose = findViewById(R.id.btnClose);
        TextView tvTitle = findViewById(R.id.tvFullImageTitle);

        int imageId = getIntent().getIntExtra("IMG_RES", -1);
        String imageTitle = getIntent().getStringExtra("IMG_TITLE");
        String imageUriStr = getIntent().getStringExtra("image_uri");

        if (imageId != -1) {
            photoView.setImageResource(imageId);
        } else if (imageUriStr != null) {
            try {
                photoView.setImageURI(Uri.parse(imageUriStr));
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            photoView.setImageResource(R.drawable.gla);
        }

        if (tvTitle != null && imageTitle != null) {
            tvTitle.setText(imageTitle);
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> onBackPressed());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}