package com.example.bluelinktest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private Vibrator vibrator;
    private boolean isProgrammaticChange = false;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initVibrator();
        initButtons(view);
        setupAirCardViewPager(view);

        // 사육장 이름 변경
        TextView txtWelcome = view.findViewById(R.id.txtWelcome);
        SharedPreferences prefs = requireContext().getSharedPreferences("HabitatPrefs", Context.MODE_PRIVATE);
        String habitatName = prefs.getString("habitat_name", "나의 사육장");
        txtWelcome.setText(habitatName);
        txtWelcome.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("사육장 이름 설정");

            final EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            input.setText(txtWelcome.getText());
            builder.setView(input);

            builder.setPositiveButton("저장", (dialog, which) -> {
                String name = input.getText().toString();
                txtWelcome.setText(name);
                prefs.edit().putString("habitat_name", name).apply();
            });
            builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        // TODO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 연동 이전 우선 하드코딩 - 연결상태
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        txtStatus.setText("Connected");
        txtStatus.setBackgroundResource(R.drawable.bg_status_connected);

        // TODO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 연동 이전 우선 하드코딩 - aircard 위 온도
        TextView txtTemp = view.findViewById(R.id.txtTemp);
        txtTemp.setText("28.4°C");

        Button btnStart = view.findViewById(R.id.btnStart);
        Button btnLock = view.findViewById(R.id.btnLock);
        Button btnUnlock = view.findViewById(R.id.btnUnlock);

        Button[] controlButtons = {btnStart, btnLock, btnUnlock};

        SwitchCompat switchAutoControl = view.findViewById(R.id.switchAutoControl);
        SharedPreferences autoPrefs = requireContext().getSharedPreferences("AutoPrefs", Context.MODE_PRIVATE);
        boolean isAuto = autoPrefs.getBoolean("auto_enabled", false);

        switchAutoControl.setChecked(isAuto);
        switchAutoControl.setText(isAuto ? "자동제어 ON" : "자동제어 OFF");
        switchAutoControl.setTrackTintList(ColorStateList.valueOf(
                Color.parseColor(isAuto ? "#A8DB5B" : "#A0A0A0")));


        switchAutoControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchAutoControl.setText(isChecked ? "자동제어 ON" : "자동제어 OFF");
            switchAutoControl.setTrackTintList(ColorStateList.valueOf(
                    Color.parseColor(isChecked ? "#A8DB5B" : "#A0A0A0")));

            if (!isProgrammaticChange) {
                for (Button btn : controlButtons) {
                    if (isChecked) {
                        btn.setBackgroundResource(R.drawable.round_button_on);
                        btn.setTag(true);
                    } else {
                        btn.setBackgroundResource(R.drawable.ripple_button);
                        btn.setTag(false);
                    }
                }
            }

            autoPrefs.edit().putBoolean("auto_enabled", isChecked).apply();
        });

        return view;
    }

    private void initVibrator() {
        if (getContext() == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager manager = (VibratorManager) requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = manager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    private void vibrate() {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }
    }

    private void initButtons(View root) {
        setupLongPressWithRipple(root, R.id.btnStart, "가습기를 켰습니다.");
        setupLongPressWithRipple(root, R.id.btnLock, "히팅램프를 켰습니다.");
        setupLongPressWithRipple(root, R.id.btnUnlock, "먹이를 줬습니다.");

        root.findViewById(R.id.btnStart).setTag(false);
        root.findViewById(R.id.btnLock).setTag(false);
        root.findViewById(R.id.btnUnlock).setTag(false);
    }

    private void setupLongPressWithRipple(View root, int buttonId, String message) {
        Button button = root.findViewById(buttonId);
        if (button == null) return;

        button.setTag(false);

        button.setOnTouchListener(new View.OnTouchListener() {
            private boolean isPressed = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPressed = true;
                        v.postDelayed(() -> {
                            if (isPressed) {
                                vibrate();

                                boolean isOn = v.getTag() != null && (boolean) v.getTag();
                                SwitchCompat switchAutoControl = requireView().findViewById(R.id.switchAutoControl);
                                Button btnStart = root.findViewById(R.id.btnStart);
                                Button btnLock = root.findViewById(R.id.btnLock);
                                Button btnUnlock = root.findViewById(R.id.btnUnlock);

                                if (!isOn) {
                                    v.setBackgroundResource(R.drawable.round_button_on);
                                    v.setTag(true);

                                    boolean allOn = (boolean) btnStart.getTag()
                                            && (boolean) btnLock.getTag()
                                            && (boolean) btnUnlock.getTag();

                                    if (allOn && !switchAutoControl.isChecked()) {
                                        isProgrammaticChange = true;
                                        switchAutoControl.setChecked(true);
                                        isProgrammaticChange = false;
                                    }

                                } else {
                                    v.setBackgroundResource(R.drawable.ripple_button);
                                    v.setTag(false);

                                    if (switchAutoControl.isChecked()) {
                                        isProgrammaticChange = true;
                                        switchAutoControl.setChecked(false);
                                        isProgrammaticChange = false;
                                    }
                                }
                            }
                        }, 1000);
                        v.setPressed(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isPressed = false;
                        v.setPressed(false);
                        break;
                }
                return true;
            }
        });
    }

    private void setupAirCardViewPager(View root) {
        ViewPager2 airViewPager = root.findViewById(R.id.airViewPager);
        List<AirCard> cardList = new ArrayList<>();

        // TODO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 연동 이전 우선 하드코딩
        cardList.add(new AirCard(R.drawable.ic_temp, "25.0°C", "현재 온도", R.drawable.bg_rounded_card));
        cardList.add(new AirCard(R.drawable.ic_sun, "80%", "현재 습도", R.drawable.bg_rounded_card_orange));
        cardList.add(new AirCard(R.drawable.ic_snow, "18:42", "마지막 급여시간", R.drawable.bg_rounded_card_blue));

        AirCardAdapter adapter = new AirCardAdapter(cardList);
        airViewPager.setAdapter(adapter);

        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.viewpager_page_margin);
        int offsetPx = getResources().getDimensionPixelOffset(R.dimen.viewpager_offset);

        airViewPager.setOffscreenPageLimit(3);
        airViewPager.setClipToPadding(false);
        airViewPager.setClipChildren(false);
        airViewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        airViewPager.setPageTransformer((page, position) -> {
            float scale = 0.9f + (1 - Math.abs(position)) * 0.2f;
            page.setScaleX(scale);
            page.setScaleY(scale);
            page.setTranslationX(-position * offsetPx);
        });

        RecyclerView recyclerView = (RecyclerView) airViewPager.getChildAt(0);
        recyclerView.setPadding(offsetPx, 0, offsetPx, 0);
        recyclerView.setClipToPadding(false);
        recyclerView.setClipChildren(false);

        airViewPager.addItemDecoration(new HorizontalMarginItemDecoration(requireContext(), R.dimen.viewpager_page_margin));

        airViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                adapter.setCenterPosition(position);
            }
        });

        // TODO 버튼 오류 --- 스위치로 해결
//        adapter.setOnItemLongPressListener(position -> {
//            vibrate();
//            String message;
//            switch (position) {
//                case 0: message = "🌡 온도 데이터 확인됨"; break;
//                case 1: message = "💧 습도 상태 확인됨"; break;
//                case 2: message = "🍚 마지막 급여시간 표시됨"; break;
//                default: message = "알 수 없음";
//            }
//            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//        });

        adapter.setOnItemLongPressListener(position -> {
            vibrate();

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_bottom,  // 들어올 때 애니메이션
                    R.anim.slide_out_top,    // 나갈 때 애니메이션
                    R.anim.slide_in_bottom,  // 뒤로 돌아올 때 들어오는 애니메이션
                    R.anim.slide_out_top     // 뒤로 돌아갈 때 나가는 애니메이션
            );
            transaction.replace(R.id.fragment_container, new SettingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });


    }


}
