package ru.skypro.homework.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "ads", indexes = {
        @Index(name = "idx_ads_user_id", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @Column(name = "price")
    @PositiveOrZero
    private int price;

    @Column(name = "ad_image")
    private String adImage;

    @Column(name = "title",nullable = false, length = 64)
    private String title;

    @Column(name = "description",nullable = false, length = 64)
    private String description;

}
