package com.gla.glaways;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class PanoramaACtivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private PanoramaView panoramaView;
    private Bitmap panoramaBitmap;
    private float currentXOffset = 0;
    private float lastTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        // Fullscreen Mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_panorama);

        String imageName = getIntent().getStringExtra("IMAGE_NAME");
        if (imageName == null) imageName = "b1";

        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        panoramaBitmap = BitmapFactory.decodeResource(getResources(), resId);

        // Custom View ko container mein add karna
        FrameLayout container = findViewById(R.id.panoramaContainer);
        if (container != null && panoramaBitmap != null) {
            panoramaView = new PanoramaView(this);
            container.addView(panoramaView);
        }

        // Close button logic
        ImageButton btnClose = findViewById(R.id.btnClose);
        if (btnClose != null) btnClose.setOnClickListener(v -> finish());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (lastTimestamp != 0) {
                final float dT = (event.timestamp - lastTimestamp) * 1.0f / 1000000000.0f;

                // Straight phone (Portrait) ke liye values[1] rotation handle karta hai
                float rotationY = event.values[1];

                // Gyroscope speed - isse image move hoti hai
                currentXOffset -= rotationY * dT * 1200;

                if (panoramaView != null) panoramaView.invalidate();
            }
            lastTimestamp = event.timestamp;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // --- INNER CLASS FOR FULL SCREEN VIEW WITH TOUCH SUPPORT ---
    private class PanoramaView extends View {
        private Matrix matrix = new Matrix();
        private float lastTouchX;

        @SuppressLint("ClickableViewAccessibility")
        public PanoramaView(Context context) {
            super(context);

            // TOUCH LOGIC: Finger scroll enable karne ke liye
            this.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchX = event.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float currentTouchX = event.getX();
                        float deltaX = currentTouchX - lastTouchX;

                        // currentXOffset update ho raha hai (0.6f is sensitivity)
                        // Isse image finger ke saath move karegi
                        currentXOffset += deltaX * 0.6f;

                        lastTouchX = currentTouchX;
                        invalidate(); // Redraw call karega
                        break;
                }
                return true; // Touch consume kar liya gaya
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (panoramaBitmap == null) return;

            float viewHeight = (float) getHeight();
            float bitmapHeight = (float) panoramaBitmap.getHeight();
            float bitmapWidth = (float) panoramaBitmap.getWidth();

            // 1. HEIGHT MATCHING SCALE
            float scale = viewHeight / bitmapHeight;
            float scaledWidth = bitmapWidth * scale;

            // 2. Seamless Loop Logic
            float xPos = (currentXOffset * scale) % scaledWidth;

            matrix.reset();
            matrix.postScale(scale, scale);

            // Draw First Copy
            matrix.postTranslate(xPos, 0);
            canvas.drawBitmap(panoramaBitmap, matrix, null);

            // Draw Second Copy (Seamless looping ke liye)
            if (xPos > 0) {
                matrix.postTranslate(-scaledWidth, 0);
            } else {
                matrix.postTranslate(scaledWidth, 0);
            }
            canvas.drawBitmap(panoramaBitmap, matrix, null);
        }
    }
}