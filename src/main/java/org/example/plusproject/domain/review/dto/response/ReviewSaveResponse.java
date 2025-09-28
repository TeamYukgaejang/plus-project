package org.example.plusproject.domain.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.user.entity.User;

@Getter
@RequiredArgsConstructor
public class ReviewSaveResponse {

    private final Long id;
    private final int point;
    private final String context;
    private final Long productId;
    private final String productTitle;
    private final Long userId;
    private final String userName;

    public static ReviewSaveResponse of(Review review, Product product, User user) {
        return new  ReviewSaveResponse(
                review.getId(),
                review.getPoint(),
                review.getContent(),
                product.getId(),
                product.getName(),
                user.getId(),
                user.getNickname()
        );
    }
}
