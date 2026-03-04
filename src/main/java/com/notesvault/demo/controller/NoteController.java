package com.notesvault.demo.controller;

import com.notesvault.demo.dto.CreateNoteRequest;
import com.notesvault.demo.dto.NoteResponse;
import com.notesvault.demo.model.Note;
import com.notesvault.demo.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    //Create new Entry
    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request) {
        Note note = new Note();
        note.setContent(request.content());
        Note created = noteService.createNote(note);
        return new ResponseEntity<>(mapToResponse(created), HttpStatus.CREATED);
    }

//    //Get All
//    @GetMapping
//    public ResponseEntity<List<NoteResponse>> getAllNotes() {
//        List<NoteResponse> responses = noteService.getAllNotes().stream()
//                .map(this::mapToResponse)
//                .toList();
//        return ResponseEntity.ok(responses);
//    }

    //Get one by ID
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable UUID id) {
        Note note = noteService.getNoteById(id);
        return ResponseEntity.ok(mapToResponse(note));
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody CreateNoteRequest request) {
        Note updated = noteService.updateNote(id, request.content());
        return ResponseEntity.ok(mapToResponse(updated));
    }

    //Search by keyword
    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes(
            @RequestParam(required = false) String search) {
        // If 'search' param is present, it filters; otherwise, it lists all [cite: 17, 39]
        List<NoteResponse> responses = noteService.searchNotes(search).stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private NoteResponse mapToResponse(Note note) {
        return new NoteResponse(note.getId(), note.getContent(), note.getCreatedAt(), note.getUpdatedAt());
    }
}