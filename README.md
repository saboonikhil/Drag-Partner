# Drag Partner

Android **partner / admin** client for the Drag ride-sharing stack.

Operators log in, work **requests** and **trips**, update rider / driver / car / fare details, and manage **connections** (users, partners, places). Talks to **[Drag-API](https://github.com/saboonikhil/Drag-API)** over HTTPS.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-Android-orange.svg)](#stack)
[![AndroidX](https://img.shields.io/badge/UI-AndroidX-green.svg)](#stack)

## What you can do

| Area | Capabilities |
|---|---|
| **Auth** | Email / password partner login (`/signIn`) |
| **Ops** | Requests and trips (bottom nav); trip detail with rider, driver, car, location, fare, payment |
| **Fleet** | Add / update cabs and rides via admin API |
| **Network** | Users, partners, places; add partner |
| **Profile** | Account details, support call, logout |
| **Push** | Firebase Cloud Messaging |

## Stack

| | |
|---|---|
| Language | Java |
| UI | AndroidX (AppCompat, Material, RecyclerView, ConstraintLayout) |
| HTTP | Retrofit 2 + Gson |
| Push | Firebase Messaging |
| SDK | min 19 · compile/target 28 |
| App ID | `com.drag.partner` |
| Version | 1.0.6 (`versionCode` 7) |

## Layout

```text
app/src/main/java/com/drag/partner/
  SplashActivity · LoginActivity · MainActivity · TripDetailsActivity
  *Fragment.java     # trips, requests, connections, profile, cars, …
  network/           # Retrofit + EndPointInterface
  model/             # Partner, Cab, Request, User, …
  adapter/           # list adapters
  util/              # FCM, calendar helpers, …
```

## Setup

**Need:** Android Studio (JDK 8+), SDK 28 (or bump yourself), a running [Drag-API](https://github.com/saboonikhil/Drag-API), and your own Firebase Android apps.

### 1. API base URL

In `app/build.gradle`:

```gradle
release {
    buildConfigField "String", "BASE_URL", "\"https://api.example.com\""
}
debug {
    buildConfigField "String", "BASE_URL", "\"https://localhost:8443\""
}
```

Point both at your API host before running.

### 2. Firebase

```bash
cp app/google-services.json.example app/google-services.json
```

Replace with the file from Firebase Console for `com.drag.partner` and `com.drag.partner.debug`.  
`app/google-services.json` is gitignored.

### 3. Build

```bash
./gradlew assembleDebug
# or open in Android Studio and Run
```

```bash
./gradlew assembleRelease
```

## Related

| Repo | Role |
|---|---|
| [Drag-API](https://github.com/saboonikhil/Drag-API) | Express + MongoDB API (auth, partners, cabs, rides, payments, OTP) |
| **Drag-Partner** | This Android partner / admin app |

## Status

Public portfolio project from the AndroidX / AGP 3.5 era. Fine for reading and forking; plan on newer AGP, dependencies, and target SDK before Play shipping.

## License

[Apache License 2.0](LICENSE)
