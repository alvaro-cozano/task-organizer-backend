package com.project.management.springboot.backend.project_management.services.user_board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.management.springboot.backend.project_management.DTO.User_boardDTO;
import com.project.management.springboot.backend.project_management.entities.connection.User_board;
import com.project.management.springboot.backend.project_management.repositories.connection.User_boardRepository;
import com.project.management.springboot.backend.project_management.entities.connection.UserBoardId;

@Service
public class UserBoardServiceImpl implements UserBoardService {

    @Autowired
    private User_boardRepository userBoardRepository;

    @Override
    @Transactional
    public void updateBoardPosition(User_boardDTO UpdateUserBoardDTO) {
        UserBoardId id = new UserBoardId(UpdateUserBoardDTO.getUser_id(), UpdateUserBoardDTO.getBoard_id());
        User_board userBoard = userBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asociación usuario-tablero no encontrada"));

        userBoard.setPosX(UpdateUserBoardDTO.getPosX());
        userBoard.setPosY(UpdateUserBoardDTO.getPosY());

        userBoardRepository.save(userBoard);
    }
}
