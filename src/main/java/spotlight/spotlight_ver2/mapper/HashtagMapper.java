package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.HashtagDTO;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Hashtag;
import spotlight.spotlight_ver2.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface HashtagMapper {
    HashtagMapper INSTANCE = Mappers.getMapper(HashtagMapper.class);

    // Hashtag -> HashtagDTO
    @Mapping(target = "feeds", source = "feeds", qualifiedByName = "mapFeedsToDTOs")
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserToHashtagUserDTO")
    HashtagDTO toDTO(Hashtag hashtag);

    // HashtagDTO -> Hashtag
    @Mapping(target = "feeds", ignore = true)
    @Mapping(target = "user", ignore = true)
    Hashtag toEntity(HashtagDTO hashtagDTO);

    // Set<String> -> Set<Hashtag>
    @Named("mapStringSetToHashtagSet")
    default Set<Hashtag> mapStringSetToHashtagSet(Set<String> hashtags) {
        return hashtags.stream()
                .map(this::mapStringToHashtag)
                .collect(Collectors.toSet());
    }

    // Set<Hashtag> -> Set<String>
    @Named("mapHashtagSetToStringSet")
    default Set<String> mapHashtagSetToStringSet(Set<Hashtag> hashtags) {
        return hashtags.stream()
                .map(Hashtag::getHashtag)
                .collect(Collectors.toSet());
    }

    // String -> Hashtag
    default Hashtag mapStringToHashtag(String hashtag) {
        Hashtag newHashtag = new Hashtag();
        newHashtag.setHashtag(hashtag);
        return newHashtag;
    }

    // Hashtag -> String
    default String mapHashtagToString(Hashtag hashtag) {
        return hashtag != null ? hashtag.getHashtag() : null;
    }

    // User -> HashtagUserDTO
    @Named("mapUserToHashtagUserDTO")
    default HashtagDTO.HashtagUserDTO mapUserToHashtagUserDTO(User user) {
        if (user == null) return null;
        HashtagDTO.HashtagUserDTO userDTO = new HashtagDTO.HashtagUserDTO(user);
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    // Set<Feed> -> Set<HashtagDTO.HashtagFeedDTO>
    @Named("mapFeedsToDTOs")
    default Set<HashtagDTO.HashtagFeedDTO> mapFeedsToDTOs(Set<Feed> feeds) {
        return feeds.stream()
                .map(HashtagDTO.HashtagFeedDTO::new)
                .collect(Collectors.toSet());
    }
}