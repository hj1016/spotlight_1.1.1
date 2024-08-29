package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.entity.Feed;

@Mapper
public interface FeedMapper {
    FeedMapper INSTANCE = Mappers.getMapper(FeedMapper.class);

    FeedDTO toDTO(Feed feed);
    Feed toEntity(FeedDTO feedDTO);
}