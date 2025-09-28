package org.example.plusproject.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;


}