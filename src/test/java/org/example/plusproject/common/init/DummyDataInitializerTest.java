package org.example.plusproject.common.init;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DummyDataInitializerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
//    @Disabled("필요할때만")
    void products_5만건_insert_테스트() {
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("DELETE FROM category");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category (name, description, created_at, updated_at) VALUES (?, ?, NOW(), NOW())",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, "성능테스트카테고리");
            ps.setString(2, "성능 측정용 카테고리");
            return ps;
        }, keyHolder);

        Long categoryId = keyHolder.getKey().longValue();

        int total = 50_000;
        int batchSize = 1000;
        List<Object[]> batch = new ArrayList<>(batchSize);

        long start = System.currentTimeMillis();

        for (int i = 1; i <= total; i++) {
            batch.add(new Object[]{
                    categoryId,
                    "상품_" + i,
                    1000 + i,
                    "설명_" + i,
                    0
            });

            if (i % batchSize == 0) {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO products (category_id, name, price, content, review_count, created_at, updated_at) " +
                                "VALUES (?, ?, ?, ?, ?, NOW(), NOW())",
                        batch
                );
                batch.clear();
            }
        }

        long end = System.currentTimeMillis();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);

        System.out.println("총 소요 시간(ms) = " + (end - start));
        System.out.println("products 총 개수 = " + count);

        assertThat(count).isEqualTo(total);
    }
}
