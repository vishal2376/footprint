<div align="center">

<img src="assets/footprint_logo.png" width="150px" />

**Footprint** is a kotlin multiplatform app that turns everyday walks into an exploration game. Every street you walk, every neighborhood you visit, gets permanently "unlocked" on your personal map.

<br/>

<img src="https://img.shields.io/github/stars/vishal2376/footprint?style=for-the-badge&logo=powerpages&color=cba6f7&logoColor=D9E0EE&labelColor=302D41"/>
<img src="https://img.shields.io/github/last-commit/vishal2376/footprint?style=for-the-badge&logo=github&color=a6da95&logoColor=D9E0EE&labelColor=302D41"/>
<img src="https://img.shields.io/github/repo-size/vishal2376/footprint?style=for-the-badge&logo=dropbox&color=7dc4e4&logoColor=D9E0EE&labelColor=302D41"/>

</div>

<br/>
<br/>


## 🏁 Table of Contents

- [Motivation](#motivation)
- [Screenshots](#screenshots)
- [Features](#features)
- [Architecture](#architecture)
- [Technical Details](#technical-details)
- [How to Run](#how-to-run)
- [Future Roadmap](#future-roadmap)
- [License](#license)


## 💡 Motivation

Living in Bangalore, I often find myself taking evening walks through the city's countless lanes and neighborhoods. One day, while exploring a new area, I realized I had no idea how much of my own city I had actually seen. That thought stuck with me.

I started wondering, what if there was a way to visualize my explorations? Not just track kilometers walked, but actually see which parts of the city I've discovered and which remain unexplored.

That's how **Footprint** was born, a companion app that turns everyday walks into an exploration game. Every street you walk, every neighborhood you visit, gets permanently "unlocked" on your personal map. The unexplored areas remain hidden behind mysterious tiles, waiting to be discovered.


## 📱 Screenshots

🎬 **[Watch Demo Video](#)** *(Coming Soon)*

<br/>

<p align="center">
  <img src="assets/screenshots/home_screen.png" width="200" alt="Home Screen"/>
  <img src="assets/screenshots/map_screen.png" width="200" alt="Map Screen"/>
  <img src="assets/screenshots/tracking.png" width="200" alt="Tracking"/>
  <img src="assets/screenshots/mystery_tiles.png" width="200" alt="Mystery Tiles"/>
</p>

## ✨ Features


#### Map Reveal
- Unexplored areas are hidden behind dark, mysterious tiles with "?" symbols. As you walk, tiles reveal themselves permanently, showing the actual map underneath.

#### Live GPS Tracking
- Record your exploration tracks with real-time path drawing. See your journey unfold on the map as you move

#### Exploration Statistics
- **Total Distance** - How far you've traveled across all tracks
- **Total Duration** - Time spent exploring
- **Tiles Explored** - Number of map tiles you've unlocked
- **World Percentage** - What fraction of the world you've discovered

#### Modern UI/UX
- Dark theme with cyan/blue gradient accents
- Smooth animations (pulsing location marker, radar loading effect)
- Bento grid dashboard with animated stats
- Custom F1-style screen transitions
  
#### Gamified Experience
- The mystery tiles create curiosity. The stats create motivation. Every walk becomes a small adventure.

## 🏗️ Architecture

Footprint follows **Clean Architecture** principles with clear separation between layers:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ HomeScreen  │  │  MapScreen  │  │ Components/Effects  │  │
│  │ + ViewModel │  │ + ViewModel │  │ (Shared UI)         │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Models    │  │ Repositories│  │     Use Cases       │  │
│  │ Track,Tile  │  │ (Interfaces)│  │ GetLiveLocation     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                       Data Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │  Room DB    │  │    DAOs     │  │   Repositories      │  │
│  │ (SQLite)    │  │ Track,Tile  │  │   (Implementations) │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Project Structure

```
composeApp/
├── src/
│   ├── commonMain/
│   │   └── kotlin/
│   │       ├── data/
│   │       │   ├── datasource/     # TileStreamProvider
│   │       │   ├── local/          # Room Database, DAOs, Entities
│   │       │   └── repository/     # Repository Implementations
│   │       ├── domain/
│   │       │   ├── model/          # Track, Location, MapConfig
│   │       │   ├── repository/     # Repository Interfaces
│   │       │   └── usecase/        # GetLiveLocationUseCase
│   │       ├── presentation/
│   │       │   ├── home_screen/    # Dashboard with stats
│   │       │   ├── map_screen/     # Interactive map
│   │       │   ├── common/         # Shared components
│   │       │   ├── navigation/     # Compose Navigation
│   │       │   └── theme/          # Colors, Typography
│   │       ├── di/                 # Koin Dependency Injection
│   │       └── util/               # Format, Time, Tile utilities
│   │
│   ├── androidMain/         # Android-specific
│   │   └── kotlin/
│   │       ├── MysteryTileGenerator  # Android Canvas rendering
│   │       ├── PermissionHandler     # Android permissions
│   │       └── LocationService       # Fused Location Provider
│   │
│   └── iosMain/             # iOS-specific
│       └── kotlin/
│           ├── MysteryTileGenerator  # CoreGraphics rendering
│           ├── PermissionHandler     # CLLocationManager
│           └── LocationService       # CoreLocation
```


## 🔧 Technical Details

### Libraries & Technologies

| Category          | Library                  | Purpose                            |
|-------------------|--------------------------|------------------------------------|
| **UI**            | Compose Multiplatform    | Shared declarative UI              |
| **Navigation**    | Compose Navigation       | Type-safe navigation               |
| **Database**      | Room (KMP)               | Local persistence                  |
| **DI**            | Koin                     | Dependency injection               |
| **Networking**    | Ktor                     | OSM(Open Street Map) tile fetching |
| **Map Rendering** | MapCompose               | Tile map rendering                 |
| **Async**         | Kotlin Coroutines + Flow | Reactive data streams              |

### Key Technical Decisions

**1. Fog of War Implementation**
- Custom `TileStreamProvider` that checks tile exploration status
- Unexplored tiles render platform-specific mystery graphics (Android Canvas / iOS CoreGraphics)
- Parent tile inheritance - exploring a tile at zoom 15 also "reveals" parent tiles

**2. Tile System**
- Uses OSM standard: 256×256 pixel tiles
- Zoom levels 10-18 supported
- Tile key format: `{zoom}_{x}_{y}`
- Efficient parent-child lookup for zoom transitions

**3. Platform Abstraction**
- `expect/actual` pattern for location services, permissions, and mystery tile generation
- Shared ViewModel logic, platform-specific hardware access

**4. Performance Optimizations**
- Debounced tile reload (1000ms) to prevent flickering
- Lazy Koin injection for database to avoid startup blocking
- WAL mode enabled for Room database


## 🚀 How to Run

### Prerequisites

- **Android Studio** Ladybug (2024.2.1) or later
- **Xcode 15+** (for iOS)
- **JDK 17+**
- Android Emulator or physical device
- iOS Simulator or physical device

### Clone the Repository

```bash
git clone https://github.com/vishal2376/footprint.git
cd footprint
```

### Android

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Select `composeApp` configuration
4. Choose an Android emulator or connected device
5. Click **Run**

### iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or connected device
3. Click **Run**


## 🏁 Future Roadmap

- [ ] **Offline Maps** — Download tiles for offline exploration
- [ ] **Achievement System** — Badges for milestones (100 tiles, 10km walked, etc.)
- [ ] **Track History** — View and replay past exploration tracks
- [ ] **Heat Map** — Visualize frequently visited areas
- [ ] **Social Sharing** — Share exploration stats with friends


## 🔒 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with ❤️ by [Vishal Singh](https://github.com/vishal2376)

</div>
