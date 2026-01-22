package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

        @Mapping(target = "id", source = "userEntity.id")
        @Mapping(target = "email", source = "userEntity.userName")
        @Mapping(target = "firstName", source = "userEntity.firstName")
        @Mapping(target = "lastName", source = "userEntity.lastName")
        @Mapping(target = "phone", source = "userEntity.phone")
        @Mapping(target = "image", source = "userEntity.userImage")
        @Mapping(target = "role", source = "authEntity.role")
        User toUserDto(UserEntity userEntity, AuthEntity authEntity);

        default String mapImage(String userImage){
            if(userImage == null) return null;
            return "/images/"+userImage;
        }


    void updateUserEntity(UpdateUser dto, @MappingTarget UserEntity entity);


    UpdateUser toDtoUpdateUser(UserEntity entity);

    @Mapping(target = "userName", source = "username")
    UserEntity toUserEntity(Register register);

    AuthEntity toAuthEntity(Register register);


}
