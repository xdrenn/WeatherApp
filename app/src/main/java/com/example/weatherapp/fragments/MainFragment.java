package com.example.weatherapp.fragments;

import static android.app.AlertDialog.BUTTON_NEGATIVE;
import static android.app.AlertDialog.BUTTON_POSITIVE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.VpAdapter;
import com.example.weatherapp.adapters.WeatherModel;
import com.example.weatherapp.databinding.FragmentMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private final String API_KEY = "7f349189eb494c6187e105915222212";
    public TextView textCity;
    public TextView textTemperature;
    public TextView textCondition;
    public TextView textDate;
    public ImageView imgWeather;
    public ImageButton btnSync;
    public ImageButton btnSearch;
    public View mainView;
    VpAdapter vpAdapter;
    ViewPager2 viewPager;
    EditText cityNameInput;
    JSONObject condition = null;
    ArrayList<WeatherModel> daysForecast = new ArrayList<>();
    ArrayList<WeatherModel> hoursForecast = new ArrayList<>();
    private ActivityResultLauncher<String> pLauncher;

    private FragmentMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public ArrayList<WeatherModel> getDaysForecast() {
        return daysForecast;
    }

    public ArrayList<WeatherModel> getHoursForecast() {
        return hoursForecast;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission();

        mainView = view;
        textCity = view.findViewById(R.id.textCity);
        textTemperature = view.findViewById(R.id.textTemperature);
        textCondition = view.findViewById(R.id.textCondition);
        textDate = view.findViewById(R.id.textDate);
        imgWeather = view.findViewById(R.id.imgWeather);
        btnSync = view.findViewById(R.id.btnSync);
        btnSearch = view.findViewById(R.id.btnSearch);
        viewPager = view.findViewById(R.id.vp);

        vpAdapter = new VpAdapter(this);
        viewPager.setAdapter(vpAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Hours");
                    break;
                case 1:
                    tab.setText("Days");
                    break;
            }
        }).attach();

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.selectTab(tabLayout.getTabAt(0));
                getLocation();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchDialog();
            }
        });

        checkLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocation();
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void openSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        cityNameInput = new EditText(requireContext());

        builder.setView(cityNameInput);
        builder.setTitle("City name:");
        builder.setPositiveButton("ok", this::click);
        builder.setNegativeButton("cancel", this::click);
        builder.create().show();
    }

    public void click(DialogInterface dialog, int i) {
        switch (i) {
            case BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            case BUTTON_POSITIVE:
                String name = cityNameInput.getText().toString();
                requestWeatherData(name);
                break;
        }
    }

    private void openLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enable location");
        builder.setMessage("Do you want enable location?");
        builder.setPositiveButton("yes", this::onClick);
        builder.setNegativeButton("no", this::onClick);
        builder.create().show();
    }

    private void onClick(DialogInterface dialog, int i) {
        switch (i) {
            case BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            case BUTTON_POSITIVE:
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
        }
    }

    private void checkLocation() {
        if (isLocationEnabled()) {
            getLocation();
        } else {
            openLocationDialog();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {

        if (!isLocationEnabled()) {
            Toast.makeText(requireContext(), "Location disabled", Toast.LENGTH_LONG).show();
        }

        final boolean fineLocationAllowed = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        final boolean coarseLocationAllowed = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (fineLocationAllowed && coarseLocationAllowed) {
            return;
        }

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                requestWeatherData(String.valueOf(location));
            }};

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, locationListener); // or LocationManager.NETWORK_PROVIDER
    }



    private void permissionListener() {
        pLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        });
    }

    Boolean isPermissionGranted(String s) {
        return ContextCompat.checkSelfPermission(getActivity(), s) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener();
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void handleResponse(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);

            // set header data

            JSONObject current = responseObject.getJSONObject("current");
            JSONObject location = responseObject.getJSONObject("location");
            JSONArray forecastDays = responseObject.getJSONObject("forecast").getJSONArray("forecastday");

            textCity.setText(location.getString("name"));
            textTemperature.setText(current.getString("temp_c") + "°C");
            textDate.setText(current.getString("last_updated"));

            condition = current.getJSONObject("condition");
            textCondition.setText(condition.getString("text"));
            String s = condition.getString("icon");
            Picasso.get().load("https:".concat(s)).into(imgWeather);

            // set first day hours forecast

            JSONArray hours = forecastDays.getJSONObject(1).getJSONArray("hour");

            hoursForecast = new ArrayList<>();

            for (int i = 0; i < hours.length(); i++) {
                JSONObject hour = hours.getJSONObject(i);
                JSONObject condition = hour.getJSONObject("condition");
                String time = hour.getString("time");
                String temp = hour.getString("temp_c");
                String conditionText = condition.getString("text");
                String img = condition.getString("icon");

                hoursForecast.add(new WeatherModel(time, temp, conditionText, img));
            }

            // set days forecast

            daysForecast = new ArrayList<>();

            for (int i = 0; i < forecastDays.length(); i++) {
                JSONObject object = forecastDays.getJSONObject(i);
                JSONObject dayObject = object.getJSONObject("day");
                JSONObject conditionObject = dayObject.getJSONObject("condition");

                String time = object.getString("date");
                String temp = dayObject.getString("mintemp_c");
                String condition = conditionObject.getString("text");
                String img = conditionObject.getString("icon");

                daysForecast.add(new WeatherModel(time, temp, condition, img));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Fragment> frags = getChildFragmentManager().getFragments();

        for (Fragment frag: frags) {
            if (frag instanceof HoursFragment) {
                ((HoursFragment) frag).getAdapter().updateData(getHoursForecast());
            }
            if (frag instanceof DaysFragment) {
                ((DaysFragment) frag).getAdapter().updateData(getDaysForecast());
            }
        }
    }

    public OnCompleteListener<Location> requestWeatherData(String locationName) {
        String url = "http://api.weatherapi.com/v1/forecast.json?" + "key=" + API_KEY + "&q=" + locationName + "&days=10" + "&aqi=no&alerts=no";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, this::handleResponse,
                VolleyError -> {
                    Log.d("MyLog", "Error: " + VolleyError);
                }
        );

        queue.add(request);

        return null;
    }
}
