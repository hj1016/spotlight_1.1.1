package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.ProjectDTO;
import spotlight.spotlight_ver2.entity.Project;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-03T00:39:29+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectDTO toDTO(Project project) {
        if ( project == null ) {
            return null;
        }

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setCreator( mapUserToCreator( project.getCreator() ) );
        projectDTO.setProjectRoles( mapProjectRolesToDTOs( project.getProjectRoles() ) );
        projectDTO.setName( project.getName() );
        projectDTO.setId( project.getId() );

        return projectDTO;
    }

    @Override
    public Project toEntity(ProjectDTO projectDTO) {
        if ( projectDTO == null ) {
            return null;
        }

        Project project = new Project();

        project.setCreator( mapProjectUserDTOToUser( projectDTO.getCreator() ) );
        project.setId( projectDTO.getId() );
        project.setName( projectDTO.getName() );

        return project;
    }
}
