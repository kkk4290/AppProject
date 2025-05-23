package com.example.smartcage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bluelinktest.R;

public class SettingFragment extends Fragment {

    // 키값
    private static final String PREF_NAME = "SettingPrefs";
    private static final String TEMP_DAY = "temp_day";
    private static final String TEMP_NIGHT = "temp_night";
    private static final String HUM_DAY = "hum_day";
    private static final String HUM_NIGHT = "hum_night";
    private static final String FEED_INTERVAL = "feed_interval";


    // 주간
    private TextView textCurrentTempDay, textCurrentHumidityDay;
    private ProgressBar progressTempDay, progressHumidityDay;
    private double currentTempDay;
    private int currentHumidityDay;

    // 야간
    private TextView textCurrentTempNight, textCurrentHumidityNight;
    private ProgressBar progressTempNight, progressHumidityNight;
    private double currentTempNight;
    private int currentHumidityNight;

    private SharedPreferences prefs;
    private Spinner spinnerFeedingInterval;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        prefs = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        TextView txtWelcome = view.findViewById(R.id.txtWelcome);
        SharedPreferences prefs = requireContext().getSharedPreferences("HabitatPrefs", Context.MODE_PRIVATE);
        String habitatName = prefs.getString("habitat_name", "나의 사육장");
        txtWelcome.setText(habitatName);

        // 온도/습도 값 불러오기
        currentTempDay = Double.longBitsToDouble(prefs.getLong(TEMP_DAY, Double.doubleToLongBits(26.0)));
        currentTempNight = Double.longBitsToDouble(prefs.getLong(TEMP_NIGHT, Double.doubleToLongBits(26.0)));
        currentHumidityDay = prefs.getInt(HUM_DAY, 60);
        currentHumidityNight = prefs.getInt(HUM_NIGHT, 60);

        // 주간 UI 연결
        textCurrentTempDay = view.findViewById(R.id.textCurrentTempDay);
        progressTempDay = view.findViewById(R.id.progressTempDay);
        textCurrentHumidityDay = view.findViewById(R.id.textCurrentHumidityDay);
        progressHumidityDay = view.findViewById(R.id.progressHumidityDay);

        // 야간 UI 연결
        textCurrentTempNight = view.findViewById(R.id.textCurrentTempNight);
        progressTempNight = view.findViewById(R.id.progressTempNight);
        textCurrentHumidityNight = view.findViewById(R.id.textCurrentHumidityNight);
        progressHumidityNight = view.findViewById(R.id.progressHumidityNight);

        spinnerFeedingInterval = view.findViewById(R.id.spinnerFeedingInterval);
        int savedInterval = prefs.getInt(FEED_INTERVAL, 0);
        spinnerFeedingInterval.setSelection(savedInterval);

        spinnerFeedingInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt(FEED_INTERVAL, position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // 값 반영
        updateUI();

        // 버튼 연결 - 주간
        view.findViewById(R.id.btnTempUpDay).setOnClickListener(v -> updateTemp(true, true));
        view.findViewById(R.id.btnTempDownDay).setOnClickListener(v -> updateTemp(false, true));
        view.findViewById(R.id.btnHumUpDay).setOnClickListener(v -> updateHumidity(true, true));
        view.findViewById(R.id.btnHumDownDay).setOnClickListener(v -> updateHumidity(false, true));

        // 버튼 연결 - 야간
        view.findViewById(R.id.btnTempUpNight).setOnClickListener(v -> updateTemp(true, false));
        view.findViewById(R.id.btnTempDownNight).setOnClickListener(v -> updateTemp(false, false));
        view.findViewById(R.id.btnHumUpNight).setOnClickListener(v -> updateHumidity(true, false));
        view.findViewById(R.id.btnHumDownNight).setOnClickListener(v -> updateHumidity(false, false));

        return view;
    }

    private void updateTemp(boolean isUp, boolean isDay) {
        double step = 0.5;
        double min = 18.0, max = 34.0;

        if (isDay) {
            currentTempDay += isUp ? step : -step;
            currentTempDay = Math.max(min, Math.min(max, currentTempDay));
        } else {
            currentTempNight += isUp ? step : -step;
            currentTempNight = Math.max(min, Math.min(max, currentTempNight));
        }

        saveSettings();
        updateUI();
    }

    private void updateHumidity(boolean isUp, boolean isDay) {
        int step = 5;
        int min = 0, max = 100;

        if (isDay) {
            currentHumidityDay += isUp ? step : -step;
            currentHumidityDay = Math.max(min, Math.min(max, currentHumidityDay));
        } else {
            currentHumidityNight += isUp ? step : -step;
            currentHumidityNight = Math.max(min, Math.min(max, currentHumidityNight));
        }

        saveSettings();
        updateUI();
    }

    private void updateUI() {
        // 주간
        textCurrentTempDay.setText(String.format("%.1f°C", currentTempDay));
        progressTempDay.setProgress((int) ((currentTempDay - 18) / 16 * 100));

        textCurrentHumidityDay.setText(currentHumidityDay + "%");
        progressHumidityDay.setProgress(currentHumidityDay);

        // 야간
        textCurrentTempNight.setText(String.format("%.1f°C", currentTempNight));
        progressTempNight.setProgress((int) ((currentTempNight - 18) / 16 * 100));

        textCurrentHumidityNight.setText(currentHumidityNight + "%");
        progressHumidityNight.setProgress(currentHumidityNight);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(TEMP_DAY, Double.doubleToLongBits(currentTempDay));
        editor.putLong(TEMP_NIGHT, Double.doubleToLongBits(currentTempNight));
        editor.putInt(HUM_DAY, currentHumidityDay);
        editor.putInt(HUM_NIGHT, currentHumidityNight);
        editor.apply();
    }
}
