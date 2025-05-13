# SmartCage - 스마트 파충류 사육장 관리 앱 🦎📱

SmartCage는 파충류 및 소형 동물의 사육 환경을 관리하기 위한 안드로이드 기반 애플리케이션입니다.  
블루투스를 통해 사육장에 부착된 임베디드 장치(라즈베리 파이 등)와 연동하여 온도, 습도, 급여 기록 등을 실시간으로 모니터링하고 제어할 수 있습니다.

---

## 🎯 주요 기능

- 📡 **Bluetooth 연동**: 최초 실행 시 기기 검색 및 페어링
- 🌡 **실시간 모니터링**: 온도/습도/마지막 급여시간을 AirCard 형태로 표시
- 🔧 **자동 제어 설정**: 주야간 온습도 목표치 설정 및 자동 제어 여부 토글
- 📊 **2주 기록 그래프**: 온도 및 습도 이력을 일자별로 시각화
- 🚀 **사용자 설정 저장**: SharedPreferences로 사육장 이름, 자동 설정 등 저장
- 🖼 **깔끔한 UI**: ViewPager2, CardView, MPAndroidChart 등을 활용한 직관적 인터페이스

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java |
| Framework | Android SDK |
| Bluetooth | Android BluetoothAdapter |
| UI | ConstraintLayout, LinearLayout, ViewPager2, MPAndroidChart |
| Architecture | Single-Activity, Multi-Fragment 구조 |
| Local Storage | SharedPreferences |
| Animation / UX | Ripple Drawable, Long Press Detection, Vibrator API |

---

## 📁 프로젝트 구조
```
SmartCage/
├── app/
│ ├── src/main/java/com/example/bluelinktest/
│ │ ├── MainActivity.java
│ │ ├── DashboardFragment.java
│ │ ├── BluetoothPairingFragment.java
│ │ ├── SettingFragment.java
│ │ └── LogFragment.java
│ ├── res/
│ │ ├── layout/
│ │ ├── drawable/
│ │ └── values/
└── README.md
```
---

## 📷 스크린샷

<p>
  <img src="./introduce1.png" alt="image1" width="20%">
  <img src="./introduce2.png" alt="image2" width="20%">
</p>


---

## 📦 향후 개선 사항

- 클라우드 연동 (Firebase 등)
- 사용자 계정/다중 사육장 관리
- 사용자 친화적 UI/UX 수정
- 영상 스트리밍 및 원격 제어 추가



<br>

### 개발과정
|날짜|한 일|완료여부|
|---|---|---|
|0422|앱 기초 틀 제작, 메인 화면 제작 | ✅ |  
|0429|메인 화면 UI 수정, 설정 화면 제작, 이 외 피드백 적용 수정|✅|
|0506|로그 화면 제작, 블루투스 페어링 화면 제작, 이 외 피드백 적용 수정|✅|
|0513|로그 화면 UI 수정, 블루투스 페어링 화면 UI 수정, 이 외 피드백 적용 수정|✅|
|0520||❌|
