# 💡 Quotes App — Daily Inspiration & Quote Manager

A modern, offline-first Android application built with **Jetpack Compose**, **Dagger Hilt**, **Room Database**, **Retrofit**, and **Jetpack DataStore Preferences**.

---

## 🌟 Features

- 📜 **Infinite Paginated Quotes Stream**: Fetches quotes dynamically from [DummyJSON Quotes API](https://dummyjson.com/quotes) using pagination (`limit` & `skip`).
- 🔍 **Instant Search & Category Filtering**: Search quotes in real-time by text or author, or filter quotes by category (*Wisdom*, *Motivational*, *Love*, *Success*, *Life*, *Philosophy*).
- ❤️ **Favorites Persistence**: Save or remove favorite quotes with offline persistence in Room DB.
- ➕ **Add Custom Quotes**: Create and manage your own custom quotes saved locally.
- 🎨 **Theme Mode Switcher (Light / Dark / System Default)**: Persistent theme selection using Jetpack DataStore Preferences.
- 🌐 **Real-time Network & Offline Status Banner**: Live network state observer powered by `ConnectivityManager.NetworkCallback` displaying custom Offline/Back-Online status snackbars anchored right above the bottom navigation bar.
- 🔒 **SSL Certificate Pinning**: Enforces SSL/TLS Certificate Pinning using OkHttp `CertificatePinner` configured via `gradle.properties` and AGP `BuildConfig` fields to protect network calls against Man-In-The-Middle (MITM) attacks.
- 🔔 **Interactive Periodic Notifications**: Scheduled background quote notifications powered by WorkManager with custom expanded/collapsed `RemoteViews` layouts and direct "Share" & "Copy" notification drawer actions.
- ♿ **Accessibility First (WCAG Compliant)**: Full TalkBack screen reader support (`semantics`, `heading`, `stateDescription`, `liveRegion`), minimum 48dp touch targets, and IME keyboard focus flow.

---

## 🛠 Tech Stack & Architecture

The app follows **Clean Architecture** and **MVVM (Model-View-ViewModel)** design patterns:

### 🏛 Layered Architecture
- **UI Layer (`ui/`)**: Declarative UI built entirely in Jetpack Compose with Material Design 2.
- **Domain Layer (`domain/`)**: Pure Kotlin Use Cases encapsulating business logic (`GetQuotesUseCase`, `SearchQuotesUseCase`, `RefreshQuotesUseCase`, `ToggleFavoriteUseCase`, `GetQuoteCountUseCase`, etc.).
- **Data Layer (`data/`)**: Offline-first repository (`QuoteRepository`), Room Local Database (`QuoteDao`), and Retrofit REST API Service (`QuotesApiService`).

### 🧰 Libraries & Tools
| Technology | Usage |
| :--- | :--- |
| **Jetpack Compose** | Modern declarative UI toolkit |
| **Dagger Hilt** | Dependency injection framework |
| **Room DB** | SQLite local database with Coroutines Flow |
| **Retrofit 2 & OkHttp** | REST API networking with Gson serialization & SSL Certificate Pinning |
| **Jetpack DataStore** | Asynchronous key-value preference storage |
| **WorkManager & HiltWorker** | Background scheduled notifications |
| **Coil Compose** | Image loading library |
| **Kotlin Coroutines & Flow** | Asynchronous programming and reactive state management |

---

## 📂 Project Structure

```
com.example.quotes/
├── data/
│   ├── local/
│   │   ├── dao/QuoteDao.kt              # Room Database Access Objects
│   │   ├── database/QuotesDatabase.kt    # Room Database Configuration
│   │   ├── datastore/ThemePreferences.kt # DataStore Persistent Preferences
│   │   └── entity/QuoteEntity.kt        # Room Database Table Entities
│   ├── mapper/QuoteMapper.kt            # Entity <-> Domain Model Mappers
│   ├── remote/
│   │   ├── api/QuotesApiService.kt      # Retrofit API Interfaces
│   │   └── model/QuoteResponseDto.kt    # Remote DTO Data Models
├── di/
│   ├── DatabaseModule.kt                # Hilt Room DI Module
│   └── NetworkModule.kt                 # Hilt Retrofit/OkHttp DI Module (with SSL Pinning)
├── domain/
│   ├── model/                           # Domain Models (Quote, QuoteCategory)
│   ├── repository/QuoteRepository.kt    # Core Repository Implementation
│   └── usecase/                         # Business Logic Use Cases
├── ui/
│   ├── add/                             # Add Custom Quote Screen & ViewModel
│   ├── components/                      # Reusable Composables (QuoteCard, ThemeOptionsMenu)
│   ├── detail/                          # Quote Detail Screen
│   ├── favorites/                       # Favorites Screen & ViewModel
│   ├── navigation/                      # NavHost Navigation Graph
│   ├── quotes/                          # Main Quotes Stream Screen & ViewModel
│   └── theme/                           # App Themes & Theme ViewModel
├── util/
│   ├── NetworkConnectivityObserver.kt   # Real-time Network Observer (ConnectivityManager)
│   ├── NetworkUtil.kt                   # Image Loading & Network Utilities
│   ├── NotificationActionReceiver.kt    # BroadcastReceiver for Notification Actions
│   ├── NotificationHelper.kt            # Custom Notification Builder
│   └── Shapes.kt                        # Custom UI Component Shapes
└── worker/
    ├── QuoteNotificationWorker.kt       # Hilt WorkManager CoroutineWorker
    └── QuoteNotificationScheduler.kt    # Periodic Work Request Manager
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug / 2024.2+ or Android Studio Jellyfish/Koala
- **JDK**: JDK 17
- **Min SDK**: API 23 (Android 6.0)
- **Target SDK**: API 36 (Android 15+)

### Building the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/nitishsharmachd/quotes-compose-android.git
   cd quotes-compose-android
   ```
2. Open the project in Android Studio.
3. Build & assemble debug APK:
   ```bash
   ./gradlew :app:assembleDebug
   ```
4. Run on an emulator or physical device.

---

## 🎨 Theme & Notification Previews

- **Theme Modes**: Toggle seamlessly between Light, Dark, or System Default. Selections persist automatically across app restarts via Jetpack DataStore.
- **Custom Notifications**: Background quote notifications automatically adapt to system dark/light modes and include interactive **Share** and **Copy to Clipboard** notification shade actions.

---

## 📄 License

```
Copyright 2026 Quotes App Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
