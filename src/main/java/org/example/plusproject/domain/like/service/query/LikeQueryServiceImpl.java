package org.example.plusproject.domain.like.service.query;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.like.repository.LikeRepository;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.review.service.query.ReviewQueryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LikeQueryServiceImpl implements LikeQueryService {

    private final LikeRepository likeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long getLikeCount(Long reviewId) {

        String redisKey = "review:" + reviewId + ":likeCount";

        String cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            return Long.valueOf(cached);
        }

        Long likeCount = likeRepository.countByReviewId(reviewId);
        redisTemplate.opsForValue().set(redisKey, likeCount.toString(), Duration.ofMinutes(10));

        return likeCount;
    }
}
