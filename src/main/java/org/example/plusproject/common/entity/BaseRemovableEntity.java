package org.example.plusproject.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseRemovableEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
