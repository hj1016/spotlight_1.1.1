package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.ProjectDTO;
import spotlight.spotlight_ver2.entity.Project;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDTO toDTO(Project project);
    Project toEntity(ProjectDTO projectDTO);
}