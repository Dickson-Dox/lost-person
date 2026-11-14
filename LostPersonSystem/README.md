# Police Lost Person Information System

Java Swing desktop app (no external libraries). Requires JDK 8 or newer.

## Demo login
Username: `admin`  Password: `police123`

## Project structure
```
LostPersonSystem/
├── README.md
├── src/police/lostperson/
│   ├── LostPersonApp.java      entry point (main)
│   ├── LoginFrame.java         admin login window
│   ├── MainFrame.java          dashboard: menu bar + entry form + buttons
│   ├── PublicDisplayFrame.java public poster-style window
│   ├── RecordsDialog.java      table of saved records
│   ├── Person.java             model (one lost-person record)
│   ├── RecordStore.java        in-memory storage
│   ├── Theme.java              colors, fonts, button factory
│   ├── HeaderPanel.java        login header banner
│   ├── ShieldIcon.java         police logo placeholder
│   └── PhotoPlaceholder.java   photo placeholder
└── single-file-version/LoginFrame.java   same app in one file
```

## Build and run (terminal)
From the `LostPersonSystem` folder:

macOS / Linux
```
mkdir -p out
javac -d out src/police/lostperson/*.java
java -cp out police.lostperson.LostPersonApp
```

Windows (Command Prompt)
```
mkdir out
javac -d out src\police\lostperson\*.java
java -cp out police.lostperson.LostPersonApp
```

## Build and run (IDE)
IntelliJ / Eclipse / NetBeans: create a new Java project, copy the `src` folder
contents in, and run `police.lostperson.LostPersonApp`.

## Notes
- Records are kept in memory only and are cleared when the program exits.
- Change the emergency number in `PublicDisplayFrame.EMERGENCY_NUMBER`.
- Replace `RecordStore` internals with a database or file to persist data.
- Replace `PhotoPlaceholder` / `ShieldIcon` with real images when available.
