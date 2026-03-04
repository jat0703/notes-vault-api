package com.notesvault.demo.repository;

import com.notesvault.demo.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
    // Search: Finds notes where content contains the keyword (case-insensitive)
    List<Note> findByContentContainingIgnoreCase(String keyword);
}
