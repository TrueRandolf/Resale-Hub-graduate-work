package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entities.AdEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdMapper {

    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", source = "adImage")
    @Mapping(target = "pk", source = "id")
    Ad toAdDto(AdEntity adEntity);

    default String mapImage(String adImage) {
        if (adImage == null) return null;
        return "/images/" + adImage;
    }

    List<Ad> toAdList(List<AdEntity> adEntityList);

    default Ads toAds(List<AdEntity> adEntityList) {
        Ads ads = new Ads();
        ads.setCount(adEntityList.size());
        ads.setResults(toAdList(adEntityList));
        return ads;
    }

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "email", source = "user.userName")
    @Mapping(target = "image", source = "adImage")
    @Mapping(target = "phone", source = "user.phone")
    ExtendedAd toExtendedAd(AdEntity adEntity);

    void updateAdEntity(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);

}
