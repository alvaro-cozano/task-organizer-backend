package com.project.management.springboot.backend.project_management.controllers;

import com.project.management.springboot.backend.project_management.DTO.StatusDTO;
import com.project.management.springboot.backend.project_management.services.status.StatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", originPatterns = "*")
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/board/{boardId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<StatusDTO>> getStatusesByBoard(@PathVariable Long boardId) {
        List<StatusDTO> statuses = statusService.getStatusesByBoard(boardId);
        if (statuses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(statuses);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StatusDTO> createStatus(@Valid @RequestBody StatusDTO statusDTO) {
        StatusDTO createdStatus = statusService.createStatus(statusDTO);
        return ResponseEntity.status(201).body(createdStatus);
    }

    @PutMapping("/{statusId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StatusDTO> updateStatus(
            @PathVariable Long statusId,
            @Valid @RequestBody StatusDTO statusDTO) {
        StatusDTO updatedStatus = statusService.updateStatus(statusId, statusDTO);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{statusId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long statusId) {
        statusService.deleteStatus(statusId);
        return ResponseEntity.noContent().build();
    }
}
