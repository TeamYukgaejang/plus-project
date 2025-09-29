package org.example.plusproject.domain.search.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 new 막기
@Builder
public class SearchKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true, length = 100)
    private String keyword;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private LocalDateTime lastSearchedAt;

    public static SearchKeyword of(String keyword) {

        return new SearchKeyword(null, keyword, 1, LocalDateTime.now());
    }

    public void increaseCount() {
        this.count++;
        this.lastSearchedAt = LocalDateTime.now();
    }
}

