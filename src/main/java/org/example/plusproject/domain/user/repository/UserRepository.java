package org.example.plusproject.domain.user.repository;

import org.example.plusproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
