package com.project.management.springboot.backend.project_management.utils.mapper;

import com.project.management.springboot.backend.project_management.DTO.UserBoardReferenceDTO;
import com.project.management.springboot.backend.project_management.DTO.User_boardDTO;
import com.project.management.springboot.backend.project_management.entities.connection.User_board;

public class User_BoardMapper {

    public static User_boardDTO toDTO(User_board entity) {
        if (entity == null) {
            return null;
        }
        return new User_boardDTO(
                entity.getUser_id(),
                entity.getBoard_id(),
                entity.getPosX(),
                entity.getPosY());
    }

    public static UserBoardReferenceDTO toUserBoardReferenceDTO(User_boardDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UserBoardReferenceDTO(dto.getPosX(), dto.getPosY());
    }

    public static User_board toEntity(User_boardDTO dto) {
        if (dto == null) {
            return null;
        }
        User_board entity = new User_board();
        entity.setUser_id(dto.getUser_id());
        entity.setBoard_id(dto.getBoard_id());
        entity.setPosX(dto.getPosX());
        entity.setPosY(dto.getPosY());
        return entity;
    }
}
