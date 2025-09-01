# Minesweeper (Java Swing)

A classic **Minesweeper game** built with **Java Swing**.  
This project recreates the traditional Minesweeper gameplay with a modern UI, restart functionality, timer, and score tracking.

---

## 🎮 Features
- **Classic Gameplay**
  - Left click to reveal tiles
  - Right click to place/remove flags
  - Chording (left + right click) to reveal surrounding tiles
- **Randomized Board Generation** (first click is always safe)
- **Overlay End Screen** with:
  - Win/Loss message
  - Timer results
  - Personal best tracking (PB)
- **Restart Button** to start a new game quickly
- **Responsive UI** with layered panes and overlay shading

---

## ⚙️ Installation & Running

### Prerequisites
- **Java 11+ (JDK)**
- **Apache Commons Lang3** (for `StopWatch`)

### Clone & Run
```bash
# Clone the repository
git clone https://github.com/yourusername/minesweeper-java.git
cd minesweeper-java

# Compile
javac -cp .:commons-lang3-3.12.0.jar Main.java Minesweeper.java Tile.java Constants.java

# Run
java -cp .:commons-lang3-3.12.0.jar Main
(On Windows, replace : with ; in the classpath.)

🧩 Project Structure
  src/
 ├── Main.java        # Entry point, sets up JFrame and layered panes
 ├── Minesweeper.java # Core game logic and rendering
 ├── Tile.java        # Tile object (state: mine, clicked, flagged)
 └── Constants.java   # Configurable constants (tile size, colors, etc.)
🎯 Controls
  - Left Click → Reveal a tile
  - Right Click → Place/remove a flag
  - Left + Right Click → Reveal surrounding tiles if flags match mine count

📊 Scoring
  - Timer starts after your first click
  - Fastest completion time is saved and displayed as your PB (Personal Best)

📜 License
  This project is open-source under the MIT License.
