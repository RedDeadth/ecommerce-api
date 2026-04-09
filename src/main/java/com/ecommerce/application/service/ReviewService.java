package com.ecommerce.application.service;

import com.ecommerce.application.dto.ReviewRequest;
import com.ecommerce.application.dto.ReviewResponse;
import com.ecommerce.domain.model.Review;
import com.ecommerce.domain.repository.ProductRepository;
import com.ecommerce.domain.repository.ReviewRepository;
import com.ecommerce.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<ReviewResponse> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::toResponse).toList();
    }

    public Double getAverageRating(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    @Transactional
    public ReviewResponse createReview(String email, ReviewRequest request) {
        var user = userRepository.findByEmail(email).orElseThrow();
        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (reviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
            throw new IllegalArgumentException("Ya has dejado una review para este producto");
        }

        var review = Review.builder()
                .user(user).product(product)
                .rating(request.rating()).comment(request.comment())
                .build();
        return toResponse(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(String email, Long reviewId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review no encontrada"));
        if (!review.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("No puedes eliminar la review de otro usuario");
        }
        reviewRepository.delete(review);
    }

    private ReviewResponse toResponse(Review r) {
        return new ReviewResponse(r.getId(), r.getProduct().getId(),
                r.getUser().getFullName(), r.getRating(), r.getComment(), r.getCreatedAt());
    }
}
