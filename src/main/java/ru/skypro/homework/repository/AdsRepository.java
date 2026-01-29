package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.AdEntity;

import java.util.List;

/**
 * Репозиторий для работы с объявлениями в БД.
 *
 * <p>Кроме стандартных операций, поддерживает поиск объявлений по идентификатору
 * автора и по имени пользователя с проверкой на отсутствие метки удаления (Soft Delete).</p>
 */

public interface AdsRepository extends JpaRepository<AdEntity, Long> {

    boolean existsById(Long id);

    List<AdEntity> findAllByUser_Id(Long userId);

    List<AdEntity> findByUser_UserNameAndUserDeletedAtIsNull(String userName);

    void deleteByUser_Id(Long id);

}
