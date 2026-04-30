package com.gla.glaways;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity6 extends AppCompatActivity {

    private TextView tvTitle, tvContent;
    private String categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain6);

        // --- 1. INITIALIZING VIEWS ---
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvContent = findViewById(R.id.tvDetailContent);

        // --- 2. GETTING DATA FROM INTENT ---
        categoryType = getIntent().getStringExtra("TYPE");

        if (categoryType == null) {
            categoryType = "INFORMATION";
        }

        // --- 3. LOADING DETAILED CONTENT ---
        loadCategoryData(categoryType);
    }

    /**
     * Is function mein saara data manually likha gaya hai
     * taaki user ko detailed information mile.
     */
    private void loadCategoryData(String type) {
        tvTitle.setText(type);

        switch (type) {
            case "COURSE FEES":
                String feesData = "Detailed Fee Structure (Academic Year 2024-25):\n\n" +
                        "1. B.TECH (CSE/CS/IT):\n" +
                        "   • Tuition Fee: ₹1,85,000 / Year\n" +
                        "   • Caution Money: ₹5,000 (One-time)\n" +
                        "   • Exam Fee: ₹10,000 / Year\n\n" +
                        "2. MCA (Master of Computer Applications):\n" +
                        "   • Tuition Fee: ₹1,35,000 / Year\n" +
                        "   • Development Fee: ₹8,000 / Year\n\n" +
                        "3. MBA (All Specializations):\n" +
                        "   • Tuition Fee: ₹1,60,000 / Year\n" +
                        "   • Activity Fee: ₹5,000 / Year\n\n" +
                        "4. BCA / BBA:\n" +
                        "   • Tuition Fee: ₹85,000 / Year\n\n" +
                        "Note: Hostel and Bus fees are additional based on distance and occupancy.";
                tvContent.setText(feesData);
                break;

            case "EVENTS":
                String eventsData = "Upcoming Campus Events & Fests:\n\n" +
                        "1. DHREEZ (Annual Cultural Fest):\n" +
                        "   • Date: March 2025\n" +
                        "   • Highlights: Celebrity Night, Fashion Show, Group Dance.\n\n" +
                        "2. MAITREE (Sports Meet):\n" +
                        "   • Sports: Cricket, Football, Volleyball, Chess.\n" +
                        "   • Inter-departmental competitions held every November.\n\n" +
                        "3. TECH-HACK (Hackathon):\n" +
                        "   • 24-Hour coding challenge organized by CSED.\n" +
                        "   • Winners get ₹50,000 Cash Prize.\n\n" +
                        "4. ALUMNI MEET:\n" +
                        "   • Annual gathering of GLA Alumni in December.";
                tvContent.setText(eventsData);
                break;

            case "CLUBS":
                String clubsData = "Student Clubs & Societies:\n\n" +
                        "1. NIYANTRA (Technical Club):\n" +
                        "   • Focus: Robotics, IoT, and AI Projects.\n\n" +
                        "2. LITERARIO (Literary Club):\n" +
                        "   • Focus: Debates, Creative Writing, and Poetry.\n\n" +
                        "3. ABHIVYAKTI (Drama Club):\n" +
                        "   • Focus: Street Plays (Nukkad Natak) and Stage Shows.\n\n" +
                        "4. VERVE (Dance Club):\n" +
                        "   • Focus: Hip-hop, Classical, and Contemporary styles.\n\n" +
                        "5. GOOGLE DEVELOPER STUDENT CLUB (GDSC):\n" +
                        "   • Focus: Open source, Web, and Android development.";
                tvContent.setText(clubsData);
                break;

            case "LIBRARY":
                String libData = "Central Library Resources:\n\n" +
                        "• Timing: 8:00 AM to 10:00 PM\n" +
                        "• Total Books: 1.5 Lakh+\n" +
                        "• Digital Access: IEEE, Springer, and J-Gate subscriptions available.\n" +
                        "• E-Library Section: 50+ Dedicated computers for research.\n\n" +
                        "Borrowing Rules:\n" +
                        "• 4 Books per student for 14 days.\n" +
                        "• Fine of ₹5/day after due date.";
                tvContent.setText(libData);
                break;

            default:
                tvContent.setText("Information will be updated soon. Please contact the Administrative Office for more details.");
                break;
        }
    }

    /**
     * XML mein jo button hai uski onClick property isse linked hai
     */
    public void onBackClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}