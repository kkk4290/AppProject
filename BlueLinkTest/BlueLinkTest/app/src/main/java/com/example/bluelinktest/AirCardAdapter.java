package com.example.bluelinktest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AirCardAdapter extends RecyclerView.Adapter<AirCardAdapter.AirCardViewHolder> {

    private final List<AirCard> airCards;
    private int centerPosition = 0;

    public interface OnItemLongPressListener {
        void onItemLongPress(int position);
    }

    private OnItemLongPressListener longPressListener;

    public void setOnItemLongPressListener(OnItemLongPressListener listener) {
        this.longPressListener = listener;
    }

    public AirCardAdapter(List<AirCard> airCards) {
        this.airCards = airCards;
    }

    @NonNull
    @Override
    public AirCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_card, parent, false);
        return new AirCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AirCardViewHolder holder, int position) {
        AirCard card = airCards.get(position);
        holder.imgIcon.setImageResource(card.iconRes);
        holder.txtValue.setText(card.value);
        holder.txtLabel.setText(card.label);
        holder.itemView.setBackgroundResource(card.bgRes);
        holder.itemView.setForeground(holder.itemView.getContext().getDrawable(R.drawable.air_card_ripple));

        float scale = (position == centerPosition) ? 1.1f : 1.0f;
        holder.itemView.setScaleX(scale);
        holder.itemView.setScaleY(scale);

        holder.itemView.setOnLongClickListener(v -> {
            if (longPressListener != null) {
                longPressListener.onItemLongPress(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return airCards.size();
    }

    public void setCenterPosition(int pos) {
        this.centerPosition = pos;
        notifyDataSetChanged();
    }

    static class AirCardViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtValue, txtLabel;

        public AirCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtValue = itemView.findViewById(R.id.txtValue);
            txtLabel = itemView.findViewById(R.id.txtLabel);
        }
    }
}