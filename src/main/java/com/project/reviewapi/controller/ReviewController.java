package com.project.reviewapi.controller;

import com.project.reviewapi.dto.ReviewDTO;
import com.project.reviewapi.dto.UserDTO;
import com.project.reviewapi.repository.ReviewRepository;
import com.project.reviewapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private static final String REVIEW_NOT_FOUND_MESSAGE = "No such review with id=%d";
    private static final String USER_NOT_FOUND_MESSAGE = "No such user with email=%s";
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @GetMapping("/{reviewId}")
    ResponseEntity<ReviewDTO> getReviewById(@PathVariable("reviewId") long reviewId) {
        var review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND_MESSAGE.formatted(reviewId)));
        return ResponseEntity.ok(ReviewDTO.entityToDto(review));
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<ReviewDTO>> getByUserId(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(reviewRepository.findAllByUserId(userId).stream()
                .map(ReviewDTO::entityToDto)
                .toList());
    }

    @PostMapping
    ResponseEntity<ReviewDTO> addNewReview(@RequestBody ReviewDTO reviewDTO) {
        var persistedUser = userRepository.findUserByEmail(reviewDTO.user().email())
                .orElseGet(() -> userRepository.save(UserDTO.dtoToEntity(reviewDTO.user())));
        var review = reviewRepository.save(ReviewDTO.dtoToEntity(reviewDTO, persistedUser));
        return ResponseEntity.ok().body(ReviewDTO.entityToDto(review));
    }

    @PutMapping("/{reviewId}")
    ResponseEntity<ReviewDTO> updateReview(@PathVariable("reviewId") long reviewId, @RequestBody ReviewDTO reviewDTO) {
        var persistedUser = userRepository.findUserByEmail(reviewDTO.user().email())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(reviewDTO.user().email())));
        if (reviewRepository.existsById(reviewId)) {
            return ResponseEntity.ok(
                    ReviewDTO.entityToDto(
                            reviewRepository.save(ReviewDTO.dtoToEntity(reviewDTO, persistedUser))
                    ));
        }
        throw new EntityNotFoundException(REVIEW_NOT_FOUND_MESSAGE.formatted(reviewId));
    }

    @DeleteMapping("/{reviewId}")
    ResponseEntity<?> removeReview(@PathVariable("reviewId") long reviewId) {
        reviewRepository.findById(reviewId).ifPresentOrElse(reviewRepository::delete, () -> {
            throw new EntityNotFoundException(REVIEW_NOT_FOUND_MESSAGE.formatted(reviewId));
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(reviewRepository.findAll().stream()
                .map(ReviewDTO::entityToDto)
                .toList());
    }
}
