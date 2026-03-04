package com.notesvault.demo.controller;

import com.notesvault.demo.dto.CreateNoteRequest;
import com.notesvault.demo.model.Note;
import com.notesvault.demo.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoteService noteService;

    @Test
    void createNote_ValidRequest_Returns201() throws Exception {
        CreateNoteRequest request = new CreateNoteRequest("Automated Test Note");
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setContent("Automated Test Note");
        note.setCreatedAt(LocalDateTime.now());

        when(noteService.createNote(Mockito.any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Automated Test Note"));
    }

    @Test
    void getNotes_NoParams_ReturnsAllNotes() throws Exception {
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setContent("Hello World");

        when(noteService.searchNotes(null)).thenReturn(List.of(note));

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello World"));
    }

    @Test
    void getNotes_WithSearchParam_ReturnsFilteredNotes() throws Exception {
        Note note = new Note();
        note.setContent("Docker Note");

        when(noteService.searchNotes("Docker")).thenReturn(List.of(note));

        mockMvc.perform(get("/notes").param("search", "Docker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Docker Note"));
    }

    @Test
    void updateNote_ValidId_ReturnsUpdatedNote() throws Exception {
        UUID id = UUID.randomUUID();
        CreateNoteRequest request = new CreateNoteRequest("Updated Content");
        Note updatedNote = new Note();
        updatedNote.setId(id);
        updatedNote.setContent("Updated Content");
        updatedNote.setUpdatedAt(LocalDateTime.now());

        when(noteService.updateNote(eq(id), anyString())).thenReturn(updatedNote);

        mockMvc.perform(put("/notes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andExpect(jsonPath("$.updatedAt").exists());
    }
}
