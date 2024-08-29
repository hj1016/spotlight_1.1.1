package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.HashtagDTO;
import spotlight.spotlight_ver2.entity.Hashtag;

@Mapper
public interface HashtagMapper {
    HashtagMapper INSTANCE = Mappers.getMapper(HashtagMapper.class);

    HashtagDTO toDTO(Hashtag hashtag);
    Hashtag toEntity(HashtagDTO hashtagDTO);
}