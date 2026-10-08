# CampusConnect

**Discover Beyond Your Campus**

## Description

CampusConnect is an inter-college event discovery desktop application built with Java Swing. Students can find hackathons, workshops, competitions, cultural events, sports, clubs and notices from their own campus and from other colleges in one place.

This version is a complete frontend with in-memory dummy data. It runs without a database.

## Problem Statement

Campus information is scattered across WhatsApp groups, Instagram, posters, emails and different college websites. Students often miss opportunities **beyond their own campus**. CampusConnect centralises discovery so students can explore events organised by other colleges as easily as their own.

## Features

- Student and admin login
- Student dashboard with search, statistics, upcoming events, recommendations and notices
- **Discover Beyond Your Campus** – events from colleges other than the student’s college
- Event search, filters and sorting
- Event details, registration and bookmarks
- My Registrations and My Bookmarks
- Notice board
- Explore colleges and college-specific events
- Clubs and activities
- Admin dashboard with live counts from dummy lists
- Admin add / edit / delete for events, notices, colleges and clubs
- Registration list with filter by event

## Technologies

- Java
- Java Swing
- OOP
- ArrayList and HashMap (in-memory data)
- LocalDate / LocalTime
- CardLayout navigation

No JDBC, SQL, JavaFX, Spring or web APIs.

## Demo Login Credentials

Demo Student:

Rebecca Zachariah

Username: `student`

Password: `student123`

College: Mar Baselios College

Interest: Technology

Demo Admin:

Username: `admin`

Password: `admin123`

The demo student belongs to **Mar Baselios College**. Discover Beyond Your Campus therefore shows events organised by other colleges, and keeps Mar Baselios College events as own-campus events.

This version is an in-memory frontend. There is no JDBC, no SQL, and no database. Database integration is future scope.

## Project Structure

```
CampusConnect/
├── src/
│   ├── Main.java
│   ├── model/          User, Event, College, Notice, Club, Registration
│   ├── data/           DummyData.java
│   └── ui/             Login, MainFrame, student and admin screens, cards
└── README.md
```

All screens read and write through `DummyData`. Later, another team member can replace those lists with database access without rewriting the UI.

## How to Run

From the project root (JDK 8 or later):

```bash
mkdir out
javac -d out src/model/*.java src/data/*.java src/ui/*.java src/Main.java
java -cp out Main
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -d out (Get-ChildItem -Recurse src\*.java | ForEach-Object { $_.FullName })
java -cp out Main
```

You can also open the `src` folder in IntelliJ IDEA or Eclipse and run `Main`.

## Future Scope

- Database integration (MySQL or SQLite) in place of `DummyData`
- Persistent user accounts and real authentication
- File or image uploads for event posters
- Email reminders for registration deadlines
- Export of registrations for organisers
