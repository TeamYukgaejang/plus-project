package org.example.plusproject.domain.product.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountResetScheduler {

    private final JdbcTemplate jdbcTemplate;
    private final CacheManager cacheManager;

    /*
     * 매일 자정(00:00:00)에 조회수 리셋
     * cron: 초 분 시 일 월 요일
     * "0 0 0 * * *" = 매일 00:00:00
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetViewCount() {
        log.info("===== [자정 리셋] 조회수 초기화 시작: {} =====", LocalDateTime.now());

        try {
            // 모든 상품의 조회수를 0으로 초기화
            int updatedCount = jdbcTemplate.update(
                    "UPDATE products SET view_count = 0 WHERE deleted_at IS NULL"
            );

            log.info("===== [자정 리셋] {}개 상품의 조회수 초기화 완료 =====", updatedCount);

            // Redis 캐시 초기화 (조회수 기반 캐시 삭제)
            clearViewRelatedCache();

            log.info("===== [자정 리셋] 조회수 초기화 완료: {} =====", LocalDateTime.now());

        } catch (Exception e) {
            log.error("===== [자정 리셋] 조회수 초기화 실패 =====", e);
            throw e;
        }
    }

    /*
     * 조회수 관련 캐시 초기화
     */
    private void clearViewRelatedCache() {
        try {
            // relatedProducts 캐시 전체 삭제
            if (cacheManager.getCache("relatedProducts") != null) {
                cacheManager.getCache("relatedProducts").clear();
                log.info("===== [캐시 초기화] relatedProducts 캐시 삭제 완료 =====");
            }
        } catch (Exception e) {
            log.error("===== [캐시 초기화] 실패 =====", e);
        }
    }
}
