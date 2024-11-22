package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.response.ExhibitionResponse;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.User;

@Mapper
public interface ExhibitionMapper {
    ExhibitionMapper INSTANCE = Mappers.getMapper(ExhibitionMapper.class);

    // Exhibition -> ExhibitionDTO
    @Mapping(target = "user.id", source = "user.id")
    @Mapping(target = "feed", source = "feed", qualifiedByName = "mapFeedToExhibitionFeedDTO")
    ExhibitionDTO toDTO(Exhibition exhibition);

    // ExhibitionDTO -> Exhibition
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserIdToUser")
    @Mapping(target = "feed", ignore = true)
    Exhibition toEntity(ExhibitionDTO exhibitionDTO);

    // Exhibition -> ExhibitionResponse
    @Mapping(source = "user.id", target = "userId")
    ExhibitionResponse toResponse(Exhibition exhibition);

    // Feed -> ExhibitionFeedDTO
    @Named("mapFeedToExhibitionFeedDTO")
    default ExhibitionDTO.ExhibitionFeedDTO mapFeedToExhibitionFeedDTO(Feed feed) {
        if (feed == null) return null;
        return new ExhibitionDTO.ExhibitionFeedDTO(feed.getFeedId(), feed.getTitle());
    }

    // User의 ID를 통해 User 엔티티 생성
    @Named("mapUserIdToUser")
    default User mapUserIdToUser(ExhibitionDTO.ExhibitionUserDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setId(userDTO.getId());
        return user;
    }
}