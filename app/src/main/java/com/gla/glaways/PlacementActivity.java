package com.gla.glaways;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PlacementActivity extends AppCompatActivity {
    RecyclerView rv;
    EditText searchBar;
    PlacementAdapter adapter;
    List<PlacementModel> fullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);

        rv = findViewById(R.id.rvPlacementList);
        searchBar = findViewById(R.id.etSearchCourse);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // ✅ SARE COURSES KA UPDATED DATA
        addPlacementData();

        adapter = new PlacementAdapter(fullList, this);
        rv.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filter(s.toString()); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void addPlacementData() {
        // Data Format: Name, Placed %, Highest, Average, Total Students, Companies
        fullList.add(new PlacementModel("MCA", "40%", "12 LPA", "3.5 LPA", "200", "TCS, Wipro, Accenture, Capgemini"));
        fullList.add(new PlacementModel("B.Tech CS", "85%", "44 LPA", "6.5 LPA", "1200", "Amazon, Google, Microsoft, Adobe"));
        fullList.add(new PlacementModel("M.Tech", "50%", "18 LPA", "6.0 LPA", "80", "Intel, Qualcomm, Nvidia, Cisco"));
        fullList.add(new PlacementModel("Biotech", "55%", "9.5 LPA", "4.2 LPA", "120", "Biocon, Dr. Reddy's, Panacea Biotec"));
        fullList.add(new PlacementModel("Agriculture", "65%", "7.5 LPA", "3.8 LPA", "150", "UPL, ITC, Dhanuka Agritech, Mahyco"));
        fullList.add(new PlacementModel("LLB", "40%", "15 LPA", "5.5 LPA", "180", "Khaitan & Co., Trilegal, Luthra & Luthra"));
        fullList.add(new PlacementModel("Diploma", "75%", "5.5 LPA", "2.8 LPA", "500", "L&T, Tata Motors, Hero MotoCorp, JBM"));
        fullList.add(new PlacementModel("BCA", "45%", "8 LPA", "3.2 LPA", "300", "Tech Mahindra, Cognizant, HCL, Deloitte"));
        fullList.add(new PlacementModel("MBA", "80%", "24 LPA", "7.5 LPA", "400", "KPMG, EY, HDFC Bank, BYJU'S, Amazon"));
        fullList.add(new PlacementModel("BBA", "60%", "10 LPA", "4.5 LPA", "350", "Federal Bank, Jaro Education, Concentrix"));
    }

    private void filter(String query) {
        List<PlacementModel> filtered = new ArrayList<>();
        for (PlacementModel item : fullList) {
            if (item.getCourseName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (adapter != null) {
            adapter.updateList(filtered);
        }
    }
}