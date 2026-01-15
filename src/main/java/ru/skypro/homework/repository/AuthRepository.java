package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.AuthEntity;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByUser_UserName(String userName);
}
