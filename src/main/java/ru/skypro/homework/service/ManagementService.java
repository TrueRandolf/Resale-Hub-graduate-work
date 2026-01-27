package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.metric.BusinessMetric;

public interface ManagementService {

    /**
     * Служебный метод.
     * <p>Сбор статистики по записям пользователей в системе.</p>
     * @param authentication данные пользователя из контекста безопасности.
     */

    BusinessMetric getBusinessMetric(Authentication authentication);


    /**
     * Служебный метод (soft-delete).
     * <p>Мягкое удаление пользователя:</p>
     * <ul>
     * <li>Изменение учетных данных в БД (анонимизация)</li>
     * <li>Удаление объявлений из БД.</li>
     * <li>Удаление медиа-контента с диска.</li>
     * </ul>
     * @param id идентификатор пользователя.
     * @param authentication данные пользователя из контекста безопасности.
     */
    void softDeleteUser(Long id, Authentication authentication);

    /**
     * Служебный метод (hard-delete).
     * Жесткое удаление пользователя:
     * <ul>
     * <li>Полное удаление пользователя и всех связанных данных из БД.</li>
     * <li>Удаление медиа-контента с диска.</li>
     * </ul>
     * @param id идентификатор пользователя
     * @param authentication данные пользователя из контекста безопасности.
     */
    void hardDeleteUser(Long id, Authentication authentication);
}
