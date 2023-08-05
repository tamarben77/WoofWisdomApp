package com.example.woofwisdomapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woofwisdomapplication.DTO.Vet;

import java.util.List;

public class VetAdapter extends RecyclerView.Adapter<VetAdapter.ViewHolder> {
    private List<Vet> vetList;

    public VetAdapter(List<Vet> vetList) {
        this.vetList = vetList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vet_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vet vet = vetList.get(position);
        holder.nameTextView.setText(vet.getName());
        holder.addressTextView.setText(vet.getAddress());
        holder.ratingTextView.setText(String.valueOf(vet.getRating()));
    }

    @Override
    public int getItemCount() {
        return vetList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView addressTextView;
        public TextView ratingTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
        }
    }
}

