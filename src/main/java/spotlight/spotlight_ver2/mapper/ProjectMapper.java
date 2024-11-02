package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.ProjectDTO;
import spotlight.spotlight_ver2.entity.Project;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.entity.ProjectRole;
import spotlight.spotlight_ver2.entity.Student;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    // Project -> ProjectDTO
    @Mapping(target = "creator", source = "creator", qualifiedByName = "mapUserToCreator")
    @Mapping(target = "projectRoles", source = "projectRoles", qualifiedByName = "mapProjectRolesToDTOs")
    @Mapping(target = "feeds", ignore = true)
    @Mapping(source = "project.name", target = "name")
    ProjectDTO toDTO(Project project);

    // ProjectDTO -> Project 매핑
    @Mapping(target = "creator", source = "creator", qualifiedByName = "mapProjectUserDTOToUser")
    @Mapping(target = "feeds", ignore = true)
    @Mapping(target = "projectRoles", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    // User -> ProjectUserDTO
    @Named("mapUserToCreator")
    default ProjectDTO.ProjectUserDTO mapUserToCreator(User user) {
        if (user == null) return null;
        return new ProjectDTO.ProjectUserDTO(user);
    }

    // ProjectUserDTO -> User
    @Named("mapProjectUserDTOToUser")
    default User mapProjectUserDTOToUser(ProjectDTO.ProjectUserDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        return user;
    }

    // Map<Student, ProjectRole> -> List<ProjectProjectRoleDTO>
    @Named("mapProjectRolesToDTOs")
    default List<ProjectDTO.ProjectProjectRoleDTO> mapProjectRolesToDTOs(Map<Student, ProjectRole> projectRoles) {
        return projectRoles.values().stream()
                .map(ProjectDTO.ProjectProjectRoleDTO::new)
                .collect(Collectors.toList());
    }
}