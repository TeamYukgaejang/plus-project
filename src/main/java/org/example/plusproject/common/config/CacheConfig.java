package org.example.plusproject.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching // Cache 활성화
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "products",         // 검색 기능용
                "relatedProducts"                // 관련 상품 추천
        );
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)      // 5분 후 자동 삭제
                        .maximumSize(1000)                                   // 최대 1,000개 저장
                        .recordStats()                                       // 통계 수집 (뭔지 보자요)
        );
        return cacheManager;
    }
}

