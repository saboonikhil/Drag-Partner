# Drag Partner

Android partner / admin app for the **Drag** ride-sharing stack. Operators sign in, manage trips and booking requests, update trip details (rider, driver, car, fare), and maintain connections (users, partners, places).

Pairs with the backend API: **[Drag-API](https://github.com/saboonikhil/Drag-API)**.

## Features

- **Auth** — email / password partner login (`/signIn`)
- **Home** — navigation drawer for profile, connections, support, logout
- **Requests & Trips** — bottom navigation; trip detail with rider / driver / car / location / fare / payment
- **Connections** — users, partners, places; add partner
- **Cars & rides** — add / update cabs and rides against the admin API
- **Push** — Firebase Cloud Messaging for in-app notifications

## Stack

| Layer | Choice |
|---|---|
| Language | Java |
| UI | AndroidX (AppCompat, Material, RecyclerView, ConstraintLayout) |
| Networking | Retrofit 2 + Gson |
| Push | Firebase Messaging |
| Min / target SDK | 19 / 28 |
| App ID | `com.drag.partner` |
| Version | 1.0.6 (versionCode 7) |

## Project layout

```text
app/src/main/java/com/drag/partner/
  LoginActivity, SplashActivity, MainActivity, TripDetailsActivity
  *Fragment.java          # trips, requests, connections, profile, cars, …
  network/                # Retrofit client + EndPointInterface
  model/                  # Partner, Cab, Request, User, …
  adapter/                # list adapters
  util/                   # FCM service, calendar helpers, …
```

## Setup

### Requirements

- Android Studio (or SDK tools) with **JDK 8+**
- Android SDK matching `compileSdkVersion 28` (or raise SDK levels if you modernize the project)
- A running **[Drag-API](https://github.com/saboonikhil/Drag-API)** (or compatible) backend
- Your own Firebase Android app config

### Configure API base URL

Release and debug URLs are set in `app/build.gradle` (placeholders by default):

```gradle
release {
    buildConfigField "String", "BASE_URL", "\"https://api.example.com\""
}
debug {
    buildConfigField "String", "BASE_URL", "\"https://localhost:8443\""
}
```

Point these at your Drag-API host before running.

### Firebase

1. Create a Firebase project and register Android app ids `com.drag.partner` and `com.drag.partner.debug`.
2. Copy the example and drop in your downloaded config (this file is gitignored):

```bash
cp app/google-services.json.example app/google-services.json
# then replace with the file from Firebase Console
```

### Build & run

```bash
./gradlew assembleDebug
# or open the project in Android Studio and Run on a device / emulator
```

Release:

```bash
./gradlew assembleRelease
```

## Related repos

| Repo | Role |
|---|---|
| [Drag-API](https://github.com/saboonikhil/Drag-API) | HTTPS Express + MongoDB API (auth, partners, cabs, rides, payments, OTP) |
| **Drag-Partner** (this repo) | Android partner / admin client |

## Status

Portfolio / legacy AndroidX project (Gradle plugin 3.5 era). Expect to bump AGP, dependencies, and target SDK for current Play / tooling requirements.

## License

[Apache License 2.0](LICENSE)
