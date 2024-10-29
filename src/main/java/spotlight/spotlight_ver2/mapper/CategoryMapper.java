package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.CategoryDTO;
import spotlight.spotlight_ver2.entity.Category;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    // Category -> CategoryDTO
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "feeds", ignore = true)
    @Mapping(target = "category", ignore = true)
    CategoryDTO toDTO(Category category);

    // CategoryDTO -> Category
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "feeds", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);
}