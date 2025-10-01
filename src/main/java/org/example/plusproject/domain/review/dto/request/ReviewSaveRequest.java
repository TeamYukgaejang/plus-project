package org.example.plusproject.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewSaveRequest {

    @Min(1) @Max(5)
    private int point;

    @Size(min = 10, max = 1000)
    private String context;
}
