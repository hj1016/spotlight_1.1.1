package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;

@Mapper
public interface ExhibitionMapper {
    ExhibitionMapper INSTANCE = Mappers.getMapper(ExhibitionMapper.class);

    ExhibitionDTO toDTO(Exhibition exhibition);
    Exhibition toEntity(ExhibitionDTO exhibitionDTO);
}