package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Hashtag;
import spotlight.spotlight_ver2.entity.Project;
import spotlight.spotlight_ver2.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface FeedMapper {
    FeedMapper INSTANCE = Mappers.getMapper(FeedMapper.class);

    // Feed -> FeedDTO (중요 속성 명시적 매핑)
    @Mapping(target = "project", source = "project", qualifiedByName = "mapProjectToFeedProjectDTO")
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserToFeedUserDTO")
    @Mapping(target = "hashtags", source = "hashtags", qualifiedByName = "mapHashtagSetToDTOSet")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToFeedCategoryDTO")
    @Mapping(target = "exhibition", source = "exhibition", qualifiedByName = "mapExhibitionToFeedExhibitionDTO")
    FeedDTO toDTO(Feed feed);

    // FeedDTO -> Feed
    @Mapping(target = "hashtags", source = "hashtags", qualifiedByName = "mapDTOSetToHashtagSet")
    @Mapping(target = "category.parent", ignore = true)
    @Mapping(target = "category.feeds", ignore = true)
    @Mapping(target = "user.username", ignore = true)
    @Mapping(target = "user.email", ignore = true)
    @Mapping(target = "user.emailVerified", ignore = true)
    @Mapping(target = "user.emailCode", ignore = true)
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.role", ignore = true)
    @Mapping(target = "user.accessToken", ignore = true)
    @Mapping(target = "user.profileImage", ignore = true)
    @Mapping(target = "user.createdDate", ignore = true)
    @Mapping(target = "user.updatedDate", ignore = true)
    @Mapping(target = "user.student", ignore = true)
    @Mapping(target = "user.recruiter", ignore = true)
    @Mapping(target = "user.hashtags", ignore = true)
    @Mapping(target = "user.feeds", ignore = true)
    @Mapping(target = "user.scraps", ignore = true)
    @Mapping(target = "exhibition.user", ignore = true)
    @Mapping(target = "exhibition.feed", ignore = true)
    @Mapping(target = "project.creator", ignore = true)
    @Mapping(target = "project.feeds", ignore = true)
    @Mapping(target = "project.projectRoles", ignore = true)
    Feed toEntity(FeedDTO feedDTO);

    // User -> FeedUserDTO
    @Named("mapUserToFeedUserDTO")
    default FeedDTO.FeedUserDTO mapUserToFeedUserDTO(User user) {
        if (user == null) return null;
        FeedDTO.FeedUserDTO userDTO = new FeedDTO.FeedUserDTO();
        userDTO.setName(user.getName());
        return userDTO;
    }

    // Project -> FeedProjectDTO
    @Named("mapProjectToFeedProjectDTO")
    default FeedDTO.FeedProjectDTO mapProjectToFeedProjectDTO(Project project) {
        if (project == null) return null;
        FeedDTO.FeedProjectDTO projectDTO = new FeedDTO.FeedProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        return projectDTO;
    }

    // Category -> FeedCategoryDTO
    @Named("mapCategoryToFeedCategoryDTO")
    default FeedDTO.FeedCategoryDTO mapCategoryToFeedCategoryDTO(Category category) {
        if (category == null) return null;
        FeedDTO.FeedCategoryDTO categoryDTO = new FeedDTO.FeedCategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    // Exhibition -> FeedExhibitionDTO
    @Named("mapExhibitionToFeedExhibitionDTO")
    default FeedDTO.FeedExhibitionDTO mapExhibitionToFeedExhibitionDTO(Exhibition exhibition) {
        if (exhibition == null) return null;
        FeedDTO.FeedExhibitionDTO exhibitionDTO = new FeedDTO.FeedExhibitionDTO();
        exhibitionDTO.setExhibitionId(exhibition.getExhibitionId());
        exhibitionDTO.setLocation(exhibition.getLocation());
        return exhibitionDTO;
    }

    // Set<FeedHashtagDTO> -> Set<Hashtag>
    @Named("mapDTOSetToHashtagSet")
    default Set<Hashtag> mapDTOSetToHashtagSet(Set<FeedDTO.FeedHashtagDTO> hashtagDTOs) {
        return hashtagDTOs.stream()
                .map(this::mapDTOToHashtag)
                .collect(Collectors.toSet());
    }

    // FeedHashtagDTO -> Hashtag
    default Hashtag mapDTOToHashtag(FeedDTO.FeedHashtagDTO dto) {
        Hashtag hashtag = new Hashtag();
        hashtag.setId(dto.getId());
        hashtag.setHashtag(dto.getHashtag());
        return hashtag;
    }

    // Set<Hashtag> -> Set<FeedHashtagDTO>
    @Named("mapHashtagSetToDTOSet")
    default Set<FeedDTO.FeedHashtagDTO> mapHashtagSetToDTOSet(Set<Hashtag> hashtags) {
        return hashtags.stream()
                .map(FeedDTO.FeedHashtagDTO::new)
                .collect(Collectors.toSet());
    }
}