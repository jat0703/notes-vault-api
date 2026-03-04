package com.notesvault.demo.service;

import com.notesvault.demo.exception.ResourceNotFoundException;
import com.notesvault.demo.model.Note;
import com.notesvault.demo.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NoteServiceTest {
    private final NoteRepository noteRepository = Mockito.mock(NoteRepository.class);
    private final NoteService noteService = new NoteService(noteRepository);

    @Test
    //Creates a new note and verifies the contents are saved correctly
    void createNote() {
        Note note = new Note();
        note.setContent("Test");
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note saved = noteService.createNote(note);
        assertEquals("Test", saved.getContent());
    }

    @Test
    //Tests that correct error is shown when attempting to get note by invalid ID
    void getNoteById_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(noteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> noteService.getNoteById(id));
    }

    @Test
    //Updates note and ensures content updated correctly
    void updateNote_WhenExists_UpdatesContent() {
        UUID id = UUID.randomUUID();
        Note existingNote = new Note();
        existingNote.setId(id);
        existingNote.setContent("Test");

        when(noteRepository.findById(id)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArguments()[0]);

        Note updated = noteService.updateNote(id, "Updated Test");

        assertEquals("Updated Test", updated.getContent());
        verify(noteRepository).save(existingNote);
    }

    @Test
    //Creates multiple notes -> searches for notes containing values -> ensure list is correct size and contains correct value
    void searchNotes_WithKeyword_ReturnsFilteredList() {
        Note note1 = new Note();
        note1.setContent("Cool note");
        Note note2 = new Note();
        note2.setContent("Lame note");

        when(noteRepository.findByContentContainingIgnoreCase("Cool"))
                .thenReturn(List.of(note1));

        List<Note> results = noteService.searchNotes("Cool");

        assertEquals(1, results.size());
        assertTrue(results.getFirst().getContent().contains("Cool"));
    }
}
