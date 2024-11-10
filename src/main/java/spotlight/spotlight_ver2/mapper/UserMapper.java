package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.UserDTO;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.enums.Role;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // User -> UserDTO
    @Mapping(target = "student", source = "student", qualifiedByName = "mapStudentIfRoleMatches")
    @Mapping(target = "recruiter", source = "recruiter", qualifiedByName = "mapRecruiterIfRoleMatches")
    @Mapping(target = "hashtags", source = "hashtags", qualifiedByName = "mapHashtagListToDTOList")
    @Mapping(target = "feeds", source = "feeds", qualifiedByName = "mapFeedListToDTOList")
    @Mapping(target = "scraps", source = "scraps", qualifiedByName = "mapScrapListToDTOList")
    UserDTO toDTO(User user);

    // UserDTO -> User
    @Mapping(target = "feeds", source = "feeds", qualifiedByName = "mapDTOListToFeedList")
    @Mapping(target = "hashtags", source = "hashtags", qualifiedByName = "mapDTOListToHashtagList")
    @Mapping(target = "scraps", source = "scraps", qualifiedByName = "mapDTOListToScrapList")
    @Mapping(target = "emailCode", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "accessToken", ignore = true)
    User toEntity(UserDTO userDTO);

    // Student -> UserStudentDTO
    @Named("mapStudentIfRoleMatches")
    default UserDTO.UserStudentDTO mapStudentIfRoleMatches(Student student) {
        return student != null ? new UserDTO.UserStudentDTO(student) : null;
    }

    // Recruiter -> UserRecruiterDTO
    @Named("mapRecruiterIfRoleMatches")
    default UserDTO.UserRecruiterDTO mapRecruiterIfRoleMatches(Recruiter recruiter) {
        return recruiter != null ? new UserDTO.UserRecruiterDTO(recruiter) : null;
    }

    // List<Hashtag> -> List<UserHashtagDTO>
    @Named("mapHashtagListToDTOList")
    default List<UserDTO.UserHashtagDTO> mapHashtagListToDTOList(List<Hashtag> hashtags) {
        return hashtags.stream()
                .map(UserDTO.UserHashtagDTO::new)
                .collect(Collectors.toList());
    }

    // List<UserHashtagDTO> -> List<Hashtag>
    @Named("mapDTOListToHashtagList")
    default List<Hashtag> mapDTOListToHashtagList(List<UserDTO.UserHashtagDTO> hashtagDTOs) {
        return hashtagDTOs.stream()
                .map(dto -> {
                    Hashtag hashtag = new Hashtag();
                    hashtag.setId(dto.getId());
                    hashtag.setHashtag(dto.getHashtag());
                    return hashtag;
                })
                .collect(Collectors.toList());
    }

    // List<Feed> -> List<UserFeedDTO>
    @Named("mapFeedListToDTOList")
    default List<UserDTO.UserFeedDTO> mapFeedListToDTOList(List<Feed> feeds) {
        return feeds.stream()
                .map(UserDTO.UserFeedDTO::new)
                .collect(Collectors.toList());
    }

    // List<UserFeedDTO> -> List<Feed>
    @Named("mapDTOListToFeedList")
    default List<Feed> mapDTOListToFeedList(List<UserDTO.UserFeedDTO> feedDTOs) {
        return feedDTOs.stream()
                .map(dto -> {
                    Feed feed = new Feed();
                    feed.setFeedId(dto.getFeedId());
                    feed.setTitle(dto.getTitle());
                    return feed;
                })
                .collect(Collectors.toList());
    }

    // List<Scrap> -> List<UserScrapDTO>
    @Named("mapScrapListToDTOList")
    default List<UserDTO.UserScrapDTO> mapScrapListToDTOList(List<Scrap> scraps) {
        return scraps.stream()
                .map(scrap -> {
                    UserDTO.UserScrapDTO dto = new UserDTO.UserScrapDTO(scrap);
                    if (scrap.getStageId() != null) {
                        dto.setStageId(scrap.getStageId().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // List<UserScrapDTO> -> List<Scrap>
    @Named("mapDTOListToScrapList")
    default List<Scrap> mapDTOListToScrapList(List<UserDTO.UserScrapDTO> scrapDTOs) {
        return scrapDTOs.stream()
                .map(dto -> {
                    Scrap scrap = new Scrap();
                    scrap.setScrapId(dto.getScrapId());

                    // Feed 설정
                    if (dto.getFeedTitle() != null) {
                        Feed feed = new Feed();
                        feed.setTitle(dto.getFeedTitle());
                        scrap.setFeed(feed);
                    }

                    // Stage 설정
                    if (dto.getStageId() != null) {
                        scrap.setStageId(mapLongToStage(dto.getStageId()));
                    }

                    return scrap;
                })
                .collect(Collectors.toList());
    }

    // Long -> Stage
    @Named("mapLongToStage")
    default Stage mapLongToStage(Long id) {
        if (id == null) return null;
        Stage stage = new Stage();
        stage.setId(id);
        return stage;
    }
}