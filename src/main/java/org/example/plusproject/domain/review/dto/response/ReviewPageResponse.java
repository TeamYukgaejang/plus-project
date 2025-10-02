package org.example.plusproject.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@RequiredArgsConstructor
@Builder
public class ReviewPageResponse {

    private final Double averagePoint;
    private final Page<ReviewResponse> reviews;

    public static ReviewPageResponse of(Double averagePoint, Page<ReviewResponse> reviews) {
        return ReviewPageResponse.builder()
                .averagePoint(averagePoint)
                .reviews(reviews)
                .build();
    }
}
