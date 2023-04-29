package com.example.woofwisdomapplication.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woofwisdomapplication.DTO.Vaccination;
import com.example.woofwisdomapplication.R;

import java.util.ArrayList;
import java.util.List;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.ViewHolder> {
    private List<Vaccination> vaccinations = new ArrayList<>();

    public void setVaccinations(List<Vaccination> vaccinations) {
        this.vaccinations = vaccinations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vaccination vaccination = vaccinations.get(position);
        holder.dateTextView.setText("Date: " + vaccination.getDate());
        holder.vaccinationNameTextView.setText("Vaccination Name: " + vaccination.getVaccination_name());
        holder.descriptionTextView.setText("Description: " + vaccination.getDescription());
        holder.locationTextView.setText("Location: " + vaccination.getLocation());
        holder.usernameTextView.setText("Username: " + vaccination.getUsername());
    }

    @Override
    public int getItemCount() {
        return vaccinations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView vaccinationNameTextView;
        TextView descriptionTextView;
        TextView locationTextView;
        TextView usernameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            vaccinationNameTextView = itemView.findViewById(R.id.vaccinationNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }
}
