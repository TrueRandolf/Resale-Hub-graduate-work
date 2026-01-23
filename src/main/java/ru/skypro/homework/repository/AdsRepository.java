package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.AdEntity;

import java.util.List;


public interface AdsRepository extends JpaRepository<AdEntity, Long> {
    boolean existsById(Long id);
    List<AdEntity> findAllByUser_Id(Long userId);
    List<AdEntity> findAll();
    List<AdEntity> findByUser_UserNameAndUserDeletedAtIsNull(String userName);
    void deleteByUser_Id(Long Id);

}
