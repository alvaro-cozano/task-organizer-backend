package com.project.management.springboot.backend.project_management.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.management.springboot.backend.project_management.DTO.BoardDTO;
import com.project.management.springboot.backend.project_management.services.board.BoardService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173", originPatterns = "*")
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public List<BoardDTO> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<BoardDTO> boardOptional = service.findById(id);
        if (boardOptional.isPresent()) {
            return ResponseEntity.ok(boardOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody BoardDTO boardDTO, BindingResult result) {
        // validation.validate(product, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(boardDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> update(@Valid @RequestBody BoardDTO boardDTO, BindingResult result,
            @PathVariable Long id) {
        // validation.validate(product, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Optional<BoardDTO> boardOptional = service.update(id, boardDTO);
        if (boardOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(boardOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<BoardDTO> boardOptional = service.delete(id);
        if (boardOptional.isPresent()) {
            return ResponseEntity.ok(boardOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/my-boards")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<BoardDTO>> getBoardsByCurrentUser() {
        List<BoardDTO> boards = service.findBoardsByCurrentUser();
        return ResponseEntity.ok(boards);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
