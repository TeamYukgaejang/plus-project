package org.example.plusproject.common.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyDataInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DummyDataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("DELETE FROM category");

        jdbcTemplate.update("INSERT INTO category (name, description, created_at, updated_at) VALUES (?, ?, NOW(), NOW())",
                "더미카테고리", "테스트용 카테고리");

        // 더미 데이터 준비
        int total = 50_000;
        int batchSize = 1000; // 한 번에 1000개씩 Insert
        List<Object[]> batch = new ArrayList<>(batchSize);

        for (int i = 1; i <= total; i++) {
            batch.add(new Object[]{
                    1L,                                  // category_id
                    "더미 상품명_" + i,                        // name
                    1000 + i,                             // price
                    "더미 설명_" + i,      // content
                    0                                     // review_count
            });

            // 배치 크기 채워지면 실행
            if (i % batchSize == 0) {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO products (category_id, name, price, content, review_count, created_at, updated_at) " +
                                "VALUES (?, ?, ?, ?, ?, NOW(), NOW())",
                        batch
                );
                batch.clear();
            }
        }

        System.out.println("더미 데이터 " + total + "건");
    }
}
