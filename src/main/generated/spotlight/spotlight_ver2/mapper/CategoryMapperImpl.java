package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.CategoryDTO;
import spotlight.spotlight_ver2.entity.Category;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-03T00:39:28+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDTO toDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setParentId( categoryParentId( category ) );
        categoryDTO.setName( category.getName() );
        categoryDTO.setId( category.getId() );

        return categoryDTO;
    }

    @Override
    public Category toEntity(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( categoryDTO.getId() );
        category.setName( categoryDTO.getName() );

        return category;
    }

    private Long categoryParentId(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parent = category.getParent();
        if ( parent == null ) {
            return null;
        }
        Long id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
