package com.example.weatherapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherapp.fragments.DaysFragment;
import com.example.weatherapp.fragments.HoursFragment;

public class VpAdapter extends FragmentStateAdapter {

    public VpAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new HoursFragment();
        }
        return new DaysFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
