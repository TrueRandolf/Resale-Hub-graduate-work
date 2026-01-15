package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entities.AdEntity;

import java.util.List;


public interface AdsRepository extends JpaRepository<AdEntity, Long> {
    List<AdEntity> findAllByUserId(Long userId);

}
