package com.project.management.springboot.backend.project_management.services.user_board;

import com.project.management.springboot.backend.project_management.DTO.User_boardDTO;

public interface UserBoardService {
    void updateBoardPosition(User_boardDTO updateUserBoardDTO);
}