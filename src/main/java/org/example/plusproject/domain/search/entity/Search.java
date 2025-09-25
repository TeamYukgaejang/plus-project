package com.project.search.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private LocalDateTime lastSearchedAt;

    public void increaseCount() {
        this.count++;
        this.lastSearchedAt = LocalDateTime.now();
    }
}

