package org.example.plusproject.common.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DummyDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DummyDataInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public DummyDataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("DELETE FROM category");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category (name, description, created_at, updated_at) VALUES (?, ?, NOW(), NOW())",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, "더미카테고리");
            ps.setString(2, "테스트용 카테고리");
            return ps;
        }, keyHolder);

        Long categoryId = keyHolder.getKey().longValue();

        // 더미 데이터 준비
        int total = 50_000;
        int batchSize = 1000; // 한 번에 1000개씩 Insert
        List<Object[]> batch = new ArrayList<>(batchSize);

        long start = System.currentTimeMillis();

        for (int i = 1; i <= total; i++) {
            batch.add(new Object[]{
                    categoryId,                             // category_id
                    "더미 상품명_" + i,                        // name
                    1000 + i,                               // price
                    "더미 설명_" + i,                         // content
                    100 + i,                                // review_count
                    50 + i                                  // view_count
            });

            // 배치 크기 채워지면 실행
            if (i % batchSize == 0) {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO products (category_id, name, price, content, review_count, view_count, created_at, updated_at) " +
                                "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())",
                        batch
                );
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO products (category_id, name, price, content, review_count, view_count, created_at, updated_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())",
                    batch
            );
        }

        long end = System.currentTimeMillis();

        log.info("더미 데이터 {}건 생성 완료, 소요 시간(ms): {}", total, (end - start));
    }
}