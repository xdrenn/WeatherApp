package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.WeatherAdapter;
import com.example.weatherapp.adapters.WeatherModel;
import com.example.weatherapp.databinding.FragmentHoursBinding;

import java.util.ArrayList;

public class HoursFragment extends Fragment {

    ArrayList<WeatherModel> model;
    private FragmentHoursBinding binding;
    private RecyclerView hoursView;
    private WeatherAdapter adapter;
    private MainFragment mainFragment;

    public HoursFragment() {
        // Required empty public constructor
    }

    public static HoursFragment newInstance() {
        return new HoursFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHoursBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hoursView = view.findViewById(R.id.hoursView);
        mainFragment = (MainFragment) this.getParentFragment();
        hoursView.setLayoutManager(new LinearLayoutManager(getActivity()));
        model = mainFragment.getHoursForecast();
        adapter = new WeatherAdapter(getContext(), model);
        hoursView.setAdapter(adapter);
    }

    public WeatherAdapter getAdapter() {
        return adapter;
    }
}

