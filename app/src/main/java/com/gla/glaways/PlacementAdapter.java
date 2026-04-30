package com.gla.glaways;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlacementAdapter extends RecyclerView.Adapter<PlacementAdapter.ViewHolder> {

    List<PlacementModel> list;
    Context context;

    public PlacementAdapter(List<PlacementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void updateList(List<PlacementModel> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_placement_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlacementModel model = list.get(position);
        holder.tvCourse.setText(model.getCourseName());
        holder.btnGetInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("course", model.getCourseName());
            intent.putExtra("placed", model.getPlacedPercentage());
            intent.putExtra("highest", model.getHighestPackage());
            intent.putExtra("average", model.getAveragePackage());
            intent.putExtra("students", model.getTotalStudents());
            intent.putExtra("companies", model.getTopCompanies());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourse; Button btnGetInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourse = itemView.findViewById(R.id.tvCourseTitle);
            btnGetInfo = itemView.findViewById(R.id.btnGetInfo);
        }
    }
}