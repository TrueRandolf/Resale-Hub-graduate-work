package ru.skypro.homework.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;

/**
 * Маппер для преобразования сущностей пользователя UserEntity и DTO.
 * <p>
 * Класс нормализует пути к изображениям, добавляя префикс из конфигурации
 * {@code app.images.base-url}, чтобы фронтенд, запущенный в Docker, мог корректно
 * отображать ресурсы по абсолютным путям.
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {
    @Value("${app.images.base-url}")
    protected String baseUrl;

    /**
     * Маппинг в DTO пользователя.
     * Использует префикс из конфига для формирования полного пути к изображению.
     */

    @Mapping(target = "id", source = "userEntity.id")
    @Mapping(target = "email", source = "userEntity.userName")
    @Mapping(target = "firstName", source = "userEntity.firstName")
    @Mapping(target = "lastName", source = "userEntity.lastName")
    @Mapping(target = "phone", source = "userEntity.phone")
    @Mapping(target = "image", source = "userEntity.userImage", qualifiedByName = "userImageToPath")
    @Mapping(target = "role", source = "authEntity.role")
    public abstract User toUserDto(UserEntity userEntity, AuthEntity authEntity);

    @Named("userImageToPath")
    protected String mapImage(String userImage) {
        if (userImage == null) return null;
        return baseUrl + userImage;
    }


    public abstract void updateUserEntity(UpdateUser dto, @MappingTarget UserEntity entity);


    public abstract UpdateUser toDtoUpdateUser(UserEntity entity);

    @Mapping(target = "userName", source = "username")
    public abstract UserEntity toUserEntity(Register register);

    public abstract AuthEntity toAuthEntity(Register register);


}
