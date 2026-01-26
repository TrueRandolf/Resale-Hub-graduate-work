package ru.skypro.homework.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Сущность комментария для хранения в БД.
 *
 * <p>Связана с пользователем (автором) через {@code user_id}.</p>
 *
 * <p>Связана с объявлением через {@code ad_id}.
 * При удалении объявления, все связанные с ним комментарии
 * каскадно удаляются на уровне БД.</p>
 *
 *  <p>Для ускорения поиска комментариев по пользователю или объявлению
 *  настроены соответствующие индексы в таблице.</p>
 */

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comments_ad_id", columnList = "ad_id"),
        @Index(name = "idx_comments_user_id", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AdEntity ad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "created_time", nullable = false)
    private Long createdAt;

    @Column(name = "text", nullable = false, length = 64)
    private String text;
}
