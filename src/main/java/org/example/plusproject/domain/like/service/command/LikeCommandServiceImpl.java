package org.example.plusproject.domain.like.service.command;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.exception.GlobalException;
import org.example.plusproject.domain.like.consts.LikeErrorCode;
import org.example.plusproject.domain.like.consts.LikeSuccessCode;
import org.example.plusproject.domain.like.dto.response.LikeResponse;
import org.example.plusproject.domain.like.entity.Like;
import org.example.plusproject.domain.like.repository.LikeRepository;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.review.service.query.ReviewQueryServiceImpl;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.example.plusproject.domain.user.entity.User;
import org.example.plusproject.domain.user.service.query.UserQueryServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeCommandServiceImpl implements LikeCommandService {

    private final LikeRepository likeRepository;
    private final ReviewQueryServiceImpl reviewQueryService;
    private final UserQueryServiceImpl userQueryService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public ApiResponse<Object> toggleLike(AuthUser authUser, Long reviewId) {

        User user = userQueryService.findUserById(authUser.getUserId());
        Review review = reviewQueryService.findReviewById(reviewId);

        String redisKey = "review:" + reviewId + ":likeCount";

        if(!user.getId().equals(review.getUser().getId())) {
            throw new GlobalException(LikeErrorCode.LIKE_OWN_REVIEW_FORBIDDEN);
        }

        Optional<Like> like = likeRepository.findByReviewIdAndUserId(reviewId, user.getId());

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            redisTemplate.opsForValue().decrement(redisKey);
            return ApiResponse.of(
                    LikeSuccessCode.LIKE_REMOVED,
                    LikeResponse.of(like.get())
            );
        }

        Like savedLike = likeRepository.save(Like.of(review, user));
        redisTemplate.opsForValue().increment(redisKey);
        return ApiResponse.of(
                LikeSuccessCode.LIKE_SUCCESS,
                LikeResponse.of(savedLike)
        );
    }
}
