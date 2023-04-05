package com.project.reviewapi.dto;

import com.project.reviewapi.model.Review;
import com.project.reviewapi.model.User;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ReviewDTO(long id, String text, double rating, @NotNull UserDTO user) {
    public static ReviewDTO entityToDto(Review review) {
        return new ReviewDTO(review.getId(), review.getText(), review.getRating().doubleValue(), UserDTO.entityToDto(review.getUser()));
    }

    public static Review dtoToEntity(ReviewDTO reviewDTO, User user) {
        return Review.builder()
                .id(reviewDTO.id)
                .text(reviewDTO.text)
                .rating(BigDecimal.valueOf(reviewDTO.rating))
                .user(user)
                .build();
    }
}
