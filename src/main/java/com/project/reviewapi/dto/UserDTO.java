package com.project.reviewapi.dto;

import com.project.reviewapi.model.User;

public record UserDTO(long id, String email, String name, String surname) {
    public static UserDTO entityToDto(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getSurname());
    }

    public static User dtoToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.id)
                .email(userDTO.email)
                .name(userDTO.name)
                .surname(userDTO.surname)
                .build();
    }
}
