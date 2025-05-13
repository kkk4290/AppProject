package com.example.bluelinktest;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 페어링 여부 확인
        SharedPreferences prefs = getSharedPreferences("PairingPrefs", Context.MODE_PRIVATE);
        boolean isConnected = prefs.getBoolean("is_connected", false);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isBluetoothEnabled = bluetoothAdapter != null && bluetoothAdapter.isEnabled();


        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 개발환경
        if (savedInstanceState == null) {
            // 🔍 블루투스 꺼져 있거나 연결 안됨 → 페어링 화면
            if (!isBluetoothEnabled || !isConnected) {
                loadFragment(new BluetoothPairingFragment());
            } else {
                loadFragment(new DashboardFragment());
            }
        }
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 개발환경


        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 실전
//        if (savedInstanceState == null) {
//            if (!isBluetoothEnabled || deviceAddress == null) {
//                // 블루투스 꺼짐 또는 저장된 기기 주소 없음 → 페어링 화면
//                loadFragment(new BluetoothPairingFragment());
//            } else {
//                // 페어링된 기기 목록에서 해당 기기 존재 여부 확인
//                boolean deviceFound = false;
//                for (android.bluetooth.BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
//                    if (device.getAddress().equals(deviceAddress)) {
//                        deviceFound = true;
//                        break;
//                    }
//                }
//
//                if (deviceFound) {
//                    loadFragment(new DashboardFragment());
//                } else {
//                    loadFragment(new BluetoothPairingFragment());
//                }
//            }
//        }
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 실전

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        View bottomNavBar = findViewById(R.id.bottomNavBar);

        ImageButton btnHome = bottomNavBar.findViewById(R.id.btn_home);
        ImageButton btnLog = bottomNavBar.findViewById(R.id.btn_log);
        ImageButton btnSetting = bottomNavBar.findViewById(R.id.btn_setting);

        btnHome.setOnClickListener(v -> loadFragment(new DashboardFragment()));
        btnLog.setOnClickListener(v -> loadFragment(new LogFragment()));
        btnSetting.setOnClickListener(v -> loadFragment(new SettingFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
