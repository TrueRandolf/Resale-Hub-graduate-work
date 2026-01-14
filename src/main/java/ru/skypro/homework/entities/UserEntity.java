package ru.skypro.homework.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false, length = 64)
    private String userName;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "first_name", length = 16)
    private String firstName;

    @Column(name = "last_name", length = 16)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "user_image")
    private String userImage;

}
