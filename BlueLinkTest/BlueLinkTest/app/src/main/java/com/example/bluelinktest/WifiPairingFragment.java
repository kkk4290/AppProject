package com.example.bluelinktest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WifiPairingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_pairing, container, false);

        Button btnWifiConnect = view.findViewById(R.id.btnWifiConnect);

        btnWifiConnect.setOnClickListener(v -> {
            // TODO: 입력값 검증 생략, 실제 와이파이 연결 로직은 향후 구현
            goToDashboard();
        });

        return view;
    }

    private void goToDashboard() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new DashboardFragment());
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
