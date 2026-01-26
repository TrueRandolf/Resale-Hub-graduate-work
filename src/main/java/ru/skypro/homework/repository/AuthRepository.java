package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.AuthEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с учетными данными пользователей.
 *
 * <p>Используется для поиска данных авторизации по уникальному
 * имени пользователя (username) при входе в систему.</p>
 */

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByUser_UserName(String userName);
}
