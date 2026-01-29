package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для управления профилями пользователей в БД.
 *
 * <p>Обеспечивает поиск и проверку существования пользователей по уникальному имени
 * (логину).</p>
 * <p>Подсчитывает количество существовующих и удаленных (soft-delete) пользователей.</p>
 */

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserName(String UserName);

    long countByDeletedAtIsNull();

    long countByDeletedAtIsNotNull();

}
