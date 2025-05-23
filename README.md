# Board Game: Snakes & Ladders & Ludo

## 📌 About the Project

This is a JavaFX desktop application developed as part of the course **IDATT2003 – Programming 2** at the Norwegian University of Science and Technology (NTNU).

The application allows users to play two classic board games:

- **Snakes & Ladders** (base implementation)
- **Ludo** (extended feature)

Both games support interactive gameplay, animated movement, and customization of players and boards.

---

## 🎮 Features

### ✅ General
- Graphical User Interface (GUI) using **JavaFX**
- Two classic board games in one application
- Game selection menu on startup

### 🎲 Snakes & Ladders
- Playable up to 5 players
- Custom board import via **JSON**
- Custom logic with snakes and ladders as effects
- Win condition when reaching tile 90

### 🟢 Ludo (Extended Feature)
- Functional core logic
- Color-based player assignment
- Home tile logic and animations
- Win condition when player reaches center after home tiles

### 👤 Player Profiles
- Custom player names
- Selectable icons for visual tokens
- Import/export of player profiles using **CSV**

---

## 🧠 Technologies and Concepts

- **Java 17**
- **JavaFX** (UI framework)
- **Model-View-Controller (MVC)** architecture
- Design patterns:
  - Singleton
  - Observer
  - Factory
  - Facade
- **JUnit 5** for unit testing
- **Maven** as build system

---

## ⚙️ How to Run

1. Clone the repository
2. Navigate to the project folder
3. Run mvn clean install
4. Run mvn clean javafx:run
