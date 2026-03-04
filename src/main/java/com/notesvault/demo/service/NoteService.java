package com.notesvault.demo.service;

import com.notesvault.demo.exception.ResourceNotFoundException;
import com.notesvault.demo.model.Note;
import com.notesvault.demo.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(UUID id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
    }

    public void deleteNote(UUID id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found with id: " + id);
        }
        noteRepository.deleteById(id);
    }

    public Note updateNote(UUID id, String newContent) {
        Note existingNote = getNoteById(id);
        existingNote.setContent(newContent);
        existingNote.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(existingNote);
    }

    public List<Note> searchNotes(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return noteRepository.findAll();
        }
        return noteRepository.findByContentContainingIgnoreCase(keyword);
    }
}
