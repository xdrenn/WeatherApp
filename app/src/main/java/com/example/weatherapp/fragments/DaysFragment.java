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
import com.example.weatherapp.databinding.FragmentDaysBinding;

import java.util.ArrayList;

public class DaysFragment extends Fragment {

    ArrayList<WeatherModel> model;
    private FragmentDaysBinding binding;
    private RecyclerView daysView;
    private WeatherAdapter adapter;
    private MainFragment mainFragment;

    public DaysFragment() {
        // Required empty public constructor
    }

    public static DaysFragment newInstance() {
        return new DaysFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDaysBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daysView = view.findViewById(R.id.daysView);
        mainFragment = (MainFragment) this.getParentFragment();
        daysView.setLayoutManager(new LinearLayoutManager(getActivity()));
        model = mainFragment.getDaysForecast();
        adapter = new WeatherAdapter(getContext(), model);
        daysView.setAdapter(adapter);
    }

    public WeatherAdapter getAdapter() {
        return adapter;
    }
}
