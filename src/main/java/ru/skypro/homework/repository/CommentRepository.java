package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.CommentEntity;

import java.util.List;

/**
 * Репозиторий для работы с комментариями к объявлениям.
 *
 * <p>Обеспечивает выборку всех комментариев, привязанных к конкретному объявлению
 * по его идентификатору.</p>
 */

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByAd_Id(Long adId);

}
