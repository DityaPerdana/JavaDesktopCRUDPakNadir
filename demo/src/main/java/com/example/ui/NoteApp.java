package com.example.ui;

import com.example.dao.NoteDAO;
import com.example.model.Note;
import com.example.util.DBConnectionManager;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;

public class NoteApp extends JFrame {

    private JList<Note> notesList;
    private DefaultListModel<Note> listModel;
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton newButton, saveButton, deleteButton;

    private NoteDAO noteDAO;
    private Connection connection;
    private Note currentNote;

    public NoteApp() {
        setTitle("Notes App");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Setup database connection
            connection = DBConnectionManager.getConnection();
            noteDAO = new NoteDAO(connection);

            // Initialize UI components
            initializeUI();

            // Load notes
            refreshNotesList();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Database connection error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            System.exit(1);
        }

        // Close database connection when app closes
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    DBConnectionManager.closeConnection();
                }
            }
        );
    }

    private void initializeUI() {
        // Create components
        listModel = new DefaultListModel<>();
        notesList = new JList<>(listModel);
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        titleField = new JTextField(20);
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        newButton = new JButton("New Note");
        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Notes list on the left
        JScrollPane listScrollPane = new JScrollPane(notesList);
        listScrollPane.setPreferredSize(new Dimension(200, 0));
        mainPanel.add(listScrollPane, BorderLayout.WEST);

        // Note editor on the right
        JPanel editorPanel = new JPanel(new BorderLayout());

        // Title and buttons at the top
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("Title:"), BorderLayout.WEST);
        titlePanel.add(titleField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        editorPanel.add(topPanel, BorderLayout.NORTH);
        editorPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        mainPanel.add(editorPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // Event handlers

        // Show selected note
        notesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Note selectedNote = notesList.getSelectedValue();
                if (selectedNote != null) {
                    displayNote(selectedNote);
                }
            }
        });

        // New note button
        newButton.addActionListener(e -> createNewNote());

        // Save button
        saveButton.addActionListener(e -> saveNote());

        // Delete button
        deleteButton.addActionListener(e -> deleteNote());
    }

    private void refreshNotesList() {
        try {
            List<Note> notes = noteDAO.getAll();
            listModel.clear();
            for (Note note : notes) {
                listModel.addElement(note);
            }

            if (!notes.isEmpty()) {
                notesList.setSelectedIndex(0);
            } else {
                clearEditor();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading notes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void displayNote(Note note) {
        try {
            // Fetch the full note details if needed
            currentNote = noteDAO.getById(note.getId());

            titleField.setText(currentNote.getTitle());
            contentArea.setText(currentNote.getContent());
            contentArea.setCaretPosition(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading note: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createNewNote() {
        currentNote = new Note("", "");
        clearEditor();
    }

    private void saveNote() {
        if (currentNote == null) {
            return;
        }

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a title for the note.",
                "Missing Title",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        currentNote.setTitle(title);
        currentNote.setContent(contentArea.getText());

        try {
            if (currentNote.getId() == 0) {
                // This is a new note
                noteDAO.insert(currentNote);
            } else {
                // This is an existing note
                noteDAO.update(currentNote);
            }

            refreshNotesList();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error saving note: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteNote() {
        if (currentNote == null || currentNote.getId() == 0) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this note?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                noteDAO.delete(currentNote.getId());
                currentNote = null;
                refreshNotesList();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error deleting note: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void clearEditor() {
        titleField.setText("");
        contentArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NoteApp app = new NoteApp();
            app.setVisible(true);
        });
    }
}
