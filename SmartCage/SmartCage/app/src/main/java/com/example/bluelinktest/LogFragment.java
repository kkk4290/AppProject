package com.example.bluelinktest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LogFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        TextView txtWelcome = view.findViewById(R.id.txtWelcome);
        SharedPreferences prefs = requireContext().getSharedPreferences("HabitatPrefs", Context.MODE_PRIVATE);
        String habitatName = prefs.getString("habitat_name", "나의 사육장");
        txtWelcome.setText(habitatName);

        LineChart temperatureChart = view.findViewById(R.id.chartTemperature);
        LineChart humidityChart = view.findViewById(R.id.chartHumidity);

        setupChart(temperatureChart, "°C", 18f, 40f, 3f,
                generateFakeData(25f, 35f),  // 주간 온도
                generateFakeData(22f, 30f)); // 야간 온도

        setupChart(humidityChart, "%", 0f, 100f, 20f,
                generateFakeData(40f, 80f),  // 주간 습도
                generateFakeData(30f, 60f)); // 야간 습도

        return view;
    }

    private void setupChart(LineChart chart, String unit, float yMin, float yMax, float yInterval,
                            ArrayList<Entry> dayData, ArrayList<Entry> nightData) {



        LineDataSet daySet = new LineDataSet(dayData, "주간");
        daySet.setColor(Color.parseColor("#FF6B6B"));
        daySet.setCircleColor(Color.parseColor("#FF6B6B"));
        daySet.setCircleRadius(4f);
        daySet.setLineWidth(2f);
        daySet.setDrawValues(false);

        LineDataSet nightSet = new LineDataSet(nightData, "야간");
        nightSet.setColor(Color.parseColor("#2196F3"));
        nightSet.setCircleColor(Color.parseColor("#2196F3"));
        nightSet.setCircleRadius(4f);
        nightSet.setLineWidth(2f);
        nightSet.setDrawValues(false);

        LineData lineData = new LineData(daySet, nightSet);
        chart.setData(lineData);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setDragEnabled(true);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);


        // X축 (요일)
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(14, true);
        xAxis.setValueFormatter(new DayAxisValueFormatter());


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(yMin);
        leftAxis.setAxisMaximum(yMax);
        leftAxis.setLabelCount((int) ((yMax - yMin) / yInterval) + 1, true);
        leftAxis.setTextColor(Color.DKGRAY);

        chart.getAxisRight().setEnabled(false);
        chart.invalidate();
    }

    private ArrayList<Entry> generateFakeData(float min, float max) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            float value = min + (float) (Math.random() * (max - min));
            entries.add(new Entry(i, value));
        }
        return entries;
    }

    private class DayAxisValueFormatter extends ValueFormatter {
        private final String[] dayLabels;

        public DayAxisValueFormatter() {
            dayLabels = new String[14];
            Calendar calendar = Calendar.getInstance();
            for (int i = 13; i >= 0; i--) { // 오른쪽이 오늘
                dayLabels[i] = new SimpleDateFormat("E", Locale.KOREAN).format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int index = (int) value;
            if (index >= 0 && index < dayLabels.length) {
                return dayLabels[index];
            } else {
                return "";
            }
        }
    }


}
