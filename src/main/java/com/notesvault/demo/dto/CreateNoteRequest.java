package com.notesvault.demo.dto;


import jakarta.validation.constraints.NotBlank;

public record CreateNoteRequest(
        @NotBlank(message = "Content cannot be empty")
        String content
) {}
