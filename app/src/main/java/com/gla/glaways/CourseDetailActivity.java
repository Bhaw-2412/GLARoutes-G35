package com.gla.glaways;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ((TextView)findViewById(R.id.tvDetailCourseName)).setText(getIntent().getStringExtra("course"));
        ((TextView)findViewById(R.id.tvDetailPlaced)).setText("Placed: " + getIntent().getStringExtra("placed"));
        ((TextView)findViewById(R.id.tvDetailHighest)).setText("Highest: " + getIntent().getStringExtra("highest"));
        ((TextView)findViewById(R.id.tvDetailAverage)).setText("Average: " + getIntent().getStringExtra("average"));
        ((TextView)findViewById(R.id.tvDetailStudents)).setText("Total Students: " + getIntent().getStringExtra("students"));
        ((TextView)findViewById(R.id.tvDetailCompanies)).setText(getIntent().getStringExtra("companies"));
    }
}