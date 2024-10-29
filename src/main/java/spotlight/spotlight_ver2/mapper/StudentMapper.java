package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.StudentDTO;
import spotlight.spotlight_ver2.entity.Proposal;
import spotlight.spotlight_ver2.entity.ProjectRole;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    // Student -> StudentDTO
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserToStudentUserDTO")
    @Mapping(target = "proposals", source = "proposals", qualifiedByName = "mapProposalsToDTOs")
    @Mapping(target = "projectRoles", source = "projectRoles", qualifiedByName = "mapProjectRolesToDTOs")
    @Mapping(target = "student", ignore = true)
    StudentDTO toDTO(Student student);

    @Mapping(target = "user", source = "user", qualifiedByName = "mapStudentUserDTOToUser")
    @Mapping(target = "proposals", ignore = true)
    @Mapping(target = "projectRoles", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    // StudentUserDTO -> User
    @Named("mapStudentUserDTOToUser")
    default User mapStudentUserDTOToUser(StudentDTO.StudentUserDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        return user;
    }

    // 제안서 목록 매핑
    @Named("mapProposalsToDTOs")
    default List<StudentDTO.StudentProposalDTO> mapProposalsToDTOs(List<Proposal> proposals) {
        return proposals.stream()
                .map(StudentDTO.StudentProposalDTO::new)
                .collect(Collectors.toList());
    }

    // 프로젝트 역할 목록 매핑
    @Named("mapProjectRolesToDTOs")
    default Set<StudentDTO.StudentProjectRoleDTO> mapProjectRolesToDTOs(Set<ProjectRole> projectRoles) {
        return projectRoles.stream()
                .map(StudentDTO.StudentProjectRoleDTO::new)
                .collect(Collectors.toSet());
    }

    // User -> StudentUserDTO
    @Named("mapUserToStudentUserDTO")
    default StudentDTO.StudentUserDTO mapUserToStudentUserDTO(User user) {
        if (user == null) return null;
        StudentDTO.StudentUserDTO userDTO = new StudentDTO.StudentUserDTO(user);
        return userDTO;
    }
}