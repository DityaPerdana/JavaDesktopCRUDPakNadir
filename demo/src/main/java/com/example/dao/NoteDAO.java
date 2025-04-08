package com.example.dao;

import com.example.model.Note;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private Connection connection;

    public NoteDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Note note) throws SQLException {
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (
            PreparedStatement statement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            statement.setString(1, note.getTitle());
            statement.setString(2, note.getContent());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    note.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Get all notes
    public List<Note> getAll() throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes ORDER BY created_at DESC";
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                Note note = new Note(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("content"),
                    resultSet.getTimestamp("created_at")
                );
                notes.add(note);
            }
        }
        return notes;
    }

    // Get a note by ID
    public Note getById(int id) throws SQLException {
        String sql = "SELECT * FROM notes WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Note(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    // Update a note
    public void update(Note note) throws SQLException {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, note.getTitle());
            statement.setString(2, note.getContent());
            statement.setInt(3, note.getId());
            statement.executeUpdate();
        }
    }

    // Delete a note
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
