package com.project.management.springboot.backend.project_management.services.user_board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.management.springboot.backend.project_management.DTO.User_boardDTO;
import com.project.management.springboot.backend.project_management.repositories.connection.User_boardRepository;

@Service
public class UserBoardServiceImpl implements UserBoardService {

    @Autowired
    private User_boardRepository userBoardRepository;

    @Override
    @Transactional
    public void updateBoardPosition(User_boardDTO updateUserBoardDTO) {
        if (!userBoardRepository.existsByUser_idAndBoard_id(updateUserBoardDTO.getUser_id(),
                updateUserBoardDTO.getBoard_id())) {
            throw new RuntimeException("Asociación usuario-tablero no encontrada");
        }

        userBoardRepository.updateBoardPosition(
                updateUserBoardDTO.getUser_id(),
                updateUserBoardDTO.getBoard_id(),
                updateUserBoardDTO.getPosX(),
                updateUserBoardDTO.getPosY());
    }
}
