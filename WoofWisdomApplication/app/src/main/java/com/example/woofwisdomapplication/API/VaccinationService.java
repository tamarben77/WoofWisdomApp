package com.example.woofwisdomapplication.API;

import com.example.woofwisdomapplication.DTO.Vaccination;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VaccinationService {
    @GET("/showVaccinations")
    Call<List<Vaccination>> getVaccinations();
}

