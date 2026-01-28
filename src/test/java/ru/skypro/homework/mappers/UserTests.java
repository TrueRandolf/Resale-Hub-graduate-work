package ru.skypro.homework.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.dto.users.User;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.test_utils.TestData;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserMapperImpl.class)
@ActiveProfiles("test")
public class UserTests {


    @Autowired
    private UserMapper userMapper;

    @Value("${app.images.base-url}")
    private String testBaseUrl;

    @Test
    void shouldMapUserAndAuthToUserDto() {
        UserEntity userEntity = TestData.createTestUserEntity();
        AuthEntity authEntity = TestData.createTestAuthEntity(userEntity);

        User user = userMapper.toUserDto(userEntity, authEntity);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userEntity.getId().intValue());
        assertThat(user.getEmail()).isEqualTo(userEntity.getUserName());
        assertThat(user.getRole()).isEqualTo(authEntity.getRole().name());
        assertThat(user.getImage()).isEqualTo(testBaseUrl + userEntity.getUserImage());
        assertThat(user.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(user.getPhone()).isEqualTo(userEntity.getPhone());
    }


    @Test
    void shouldUpdateUserFields() {
        UserEntity userEntity = TestData.createTestUserEntity();

        UpdateUser updateUser = new UpdateUser();
        String newFirstName = "New FirstName";
        String newLastName = "New LastName";
        String newPhone = "+755544443322";
        updateUser.setFirstName(newFirstName);
        updateUser.setLastName(newLastName);
        updateUser.setPhone(newPhone);

        userMapper.updateUserEntity(updateUser, userEntity);

        assertThat(userEntity).isNotNull();

        assertThat(userEntity.getFirstName()).isEqualTo(updateUser.getFirstName());
        assertThat(userEntity.getLastName()).isEqualTo(updateUser.getLastName());
        assertThat(userEntity.getPhone()).isEqualTo(updateUser.getPhone());

    }

    @Test
    void shouldWriteRegisterInTwoEntities() {
        Register register = new Register();
        register.setUsername("Login");
        register.setPassword("password");
        register.setFirstName("FirstName");
        register.setLastName("LastName");
        register.setPhone("+799988887766");
        register.setRole(Role.USER);

        UserEntity userEntity = userMapper.toUserEntity(register);
        AuthEntity authEntity = userMapper.toAuthEntity(register);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getUserName()).isEqualTo(register.getUsername());
        assertThat(userEntity.getFirstName()).isEqualTo(register.getFirstName());
        assertThat(userEntity.getLastName()).isEqualTo(register.getLastName());
        assertThat(userEntity.getPhone()).isEqualTo(register.getPhone());

        assertThat(authEntity).isNotNull();
        assertThat(authEntity.getPassword()).isEqualTo(register.getPassword());
        assertThat(authEntity.getRole()).isEqualTo(register.getRole());

    }

}