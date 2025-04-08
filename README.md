# Java Notes Application

A simple yet powerful desktop notes application built with Java Swing and PostgreSQL. This application allows users to create, view, edit, and delete notes with a clean and intuitive user interface.

## Features

- **User-friendly Interface**: Clean two-panel design with notes list and editor
- **Complete CRUD Operations**: Create, read, update, and delete notes
- **PostgreSQL Integration**: Persistent storage of notes in a database
- **Error Handling**: Comprehensive error management and user feedback
- **MVC Architecture**: Clean separation of concerns for maintainable code

## Screenshots

Saya terlalu malas untuk menambahkan screenshot

## Requirements

- Java JDK 11 or higher
- PostgreSQL 12 or higher
- PostgreSQL JDBC Driver

## Installation

1. Clone this repository:
   ```
   git clone https://github.com/yourusername/java-notes-app.git
   cd java-notes-app
   ```

2. Set up the PostgreSQL database:
   ```
   sudo -u postgres createdb notesdb
   ```

3. Create the notes table:
   ```sql
   CREATE TABLE notes (
     id SERIAL PRIMARY KEY,
     title VARCHAR(255) NOT NULL,
     content TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

4. Compile the application:
   ```
   javac -cp .:postgresql-42.6.0.jar com/example/Main.java
   ```

5. Run the application:
   ```
   java -cp .:postgresql-42.6.0.jar com.example.Main
   ```

## Configuration

Edit the `DBConnectionManager.java` file to configure your database connection:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/notesdb";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

## Project Structure

```
src/
├── com/
│   └── example/
│       ├── Main.java                  # Application entry point
│       ├── model/
│       │   └── Note.java              # Note data model
│       ├── dao/
│       │   └── NoteDAO.java           # Database operations
│       ├── ui/
│       │   └── NoteApp.java           # Main UI component
│       └── util/
│           └── DBConnectionManager.java # Database connection manager
```

## How It Works

The application follows the Model-View-Controller (MVC) architecture:

- **Model**: The `Note` class represents the data structure for a note
- **View**: The `NoteApp` class handles the UI components and user interaction
- **Controller**: The `NoteDAO` class manages database operations

When the application starts, it:
1. Establishes a connection to the PostgreSQL database
2. Loads existing notes into the list view
3. Allows users to select, create, edit, or delete notes

## Troubleshooting

### Database Connection Issues

If you see the error:
```
java.sql.SQLException: No suitable driver found for jdbc:postgresql://localhost:5432/dbname
```

Make sure:
1. The PostgreSQL JDBC driver JAR is in your classpath
2. The database exists and is running
3. The database URL, username, and password are correct

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- PostgreSQL community for the excellent database system
- Java Swing for the UI components
- All contributors who have helped improve this project
