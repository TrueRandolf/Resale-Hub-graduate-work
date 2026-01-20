package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.repository.AuthRepository;

@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthEntity authEntity = authRepository.findByUser_UserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        if (authEntity.getUser().getDeletedAt() != null) {
            throw new UsernameNotFoundException("user deleted !");
        }

        UserDetails userDetails =
                User.builder()
                        .username(authEntity.getUser().getUserName())
                        .password(authEntity.getPassword())
                        .roles(authEntity.getRole().name())
                        .build();
        System.out.println("userDetails.toString() = " + userDetails.toString());
        return userDetails;

//        return User.builder()
//                .username(authEntity.getUser().getUserName())
//                .password(authEntity.getPassword())
//                .roles(authEntity.getRole().name())
//                .build();
    }
}
