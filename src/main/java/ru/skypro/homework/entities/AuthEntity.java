package ru.skypro.homework.entities;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Table(name = "auth_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEntity {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String password;

    @Column(name = "role", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private Role role;
}
