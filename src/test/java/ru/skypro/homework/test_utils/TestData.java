package ru.skypro.homework.test_utils;

import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;

public class TestData {
    private static long userId = 0L;
    private static long adId = 100L;
    private static long commentId = 1000L;
    private static final long timeMillis = 1736952000000L;

    public static UserEntity createTestUserEntity() {
        userId++;
        return UserEntity.builder()
                .id(userId)
                .userName("User_Name " + userId)
                .userImage("UserImage " + userId + " .png")
                .phone(String.format("+7999%07d", userId))
                .firstName("User Firstname " + userId)
                .lastName("User Lastname " + userId)
                .deletedAt(null)
                .build();
    }

    public static AuthEntity createTestAuthEntity(UserEntity user) {
        return AuthEntity.builder()
                .id(user.getId())
                .password("password " + user.getId())
                .role(Role.USER)
                .user(user)
                .build();
    }


    public static AdEntity createTestAdEntity(UserEntity user) {
        adId++;
        return AdEntity.builder()
                .id(adId)
                .price((int) (1000 + adId))
                .adImage("product " + adId + ".png")
                .title("title product " + adId)
                .description("description product " + adId)
                .user(user)
                .build();
    }

    public static CommentEntity createTestCommentEntity(UserEntity user, AdEntity ad) {
        commentId++;
        return CommentEntity.builder()
                .id(commentId)
                .createdAt(timeMillis)
                .text("comment text " + commentId)
                .ad(ad)
                .user(user)
                .build();
    }


}
