package com.example.bluelinktest;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BluetoothPairingFragment extends Fragment {

    private BluetoothAdapter bluetoothAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bluetooth_pairing, container, false);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Button btnStartPairing = view.findViewById(R.id.btnStartPairing);
        Button btnSkip = view.findViewById(R.id.btnSkip);

        btnStartPairing.setOnClickListener(v -> startBluetoothPairing());
//        btnSkip.setOnClickListener(v -> goToDashboard());
        btnSkip.setOnClickListener(v -> goToWifiPairing());

        // TODO 블루투스 최초 통과시 검증 과정 없앰
//        SharedPreferences prefs = requireContext().getSharedPreferences("PairingPrefs", Context.MODE_PRIVATE);
//        boolean isConnected = prefs.getBoolean("is_connected", false);
//        if (isConnected) {
//            goToDashboard();
//        }

        return view;
    }

    private void startBluetoothPairing() {
        if (bluetoothAdapter == null || bluetoothAdapter.isEnabled()) {  // TODO !bluetoothAdapter.isEnabled()
            Toast.makeText(getContext(), "블루투스가 꺼져 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔧 실제 연결 로직 대신 모의 성공 처리
        Toast.makeText(getContext(), "기기 연결 성공!", Toast.LENGTH_SHORT).show();

        // 상태 저장
        SharedPreferences prefs = requireContext().getSharedPreferences("PairingPrefs", Context.MODE_PRIVATE);

        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 개발환경
        prefs.edit().putBoolean("is_connected", true).apply();
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 개발환경

        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 실전
//        prefs.edit()
//                .putBoolean("is_connected", true)
//                .putString("device_address", "00:11:22:33:44:55")
//                .apply();
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 실전


        // 대시보드로 이동
//        goToDashboard();
        goToWifiPairing();
    }

//    private void goToDashboard() {
//        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, new DashboardFragment());
//        transaction.commit();
//    }

    // 기존 goToDashboard() → goToWifiPairing()
    private void goToWifiPairing() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new WifiPairingFragment());
        transaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        View bottomNavBar = requireActivity().findViewById(R.id.bottomNavBar);
        if (bottomNavBar != null) {
            bottomNavBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        View bottomNavBar = requireActivity().findViewById(R.id.bottomNavBar);
        if (bottomNavBar != null) {
            bottomNavBar.setVisibility(View.VISIBLE);
        }
    }

}
