package com.project.management.springboot.backend.project_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.management.springboot.backend.project_management.DTO.User_boardDTO;
import com.project.management.springboot.backend.project_management.services.user_board.UserBoardService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", originPatterns = "*")
@RequestMapping("/user-board")
public class UserBoardController {

    @Autowired
    private UserBoardService userBoardService;

    @PatchMapping("/position")
    public void updateBoardPosition(@RequestBody User_boardDTO UpdateUserBoardDTO) {
        userBoardService.updateBoardPosition(UpdateUserBoardDTO);
    }
}