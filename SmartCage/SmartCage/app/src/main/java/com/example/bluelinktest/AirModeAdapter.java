package com.example.bluelinktest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AirModeAdapter extends RecyclerView.Adapter<AirModeAdapter.ViewHolder> {

    private List<AirMode> items;
    private int selectedPosition = 0;

    public AirModeAdapter(List<AirMode> items) {
        this.items = items;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AirMode mode = items.get(position);
        holder.imgIcon.setImageResource(mode.iconResId);
        holder.txtValue.setText(mode.value);
        holder.txtLabel.setText(mode.label);
        holder.itemView.setBackgroundResource(mode.bgResId);

        float scale = (selectedPosition == position) ? 1.1f : 1.0f;
        holder.itemView.setScaleX(scale);
        holder.itemView.setScaleY(scale);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtValue, txtLabel;

        ViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtValue = itemView.findViewById(R.id.txtValue);
            txtLabel = itemView.findViewById(R.id.txtLabel);
        }
    }
}
