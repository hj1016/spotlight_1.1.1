package spotlight.spotlight_ver2.mapper;

import javax.annotation.processing.Generated;
import spotlight.spotlight_ver2.dto.StudentDTO;
import spotlight.spotlight_ver2.entity.Student;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-05T17:17:51+0900",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentDTO toDTO(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setUser( mapUserToStudentUserDTO( student.getUser() ) );
        studentDTO.setProposals( mapProposalsToDTOs( student.getProposals() ) );
        studentDTO.setProjectRoles( mapProjectRolesToDTOs( student.getProjectRoles() ) );
        studentDTO.setUserId( student.getUserId() );
        studentDTO.setSchool( student.getSchool() );
        studentDTO.setMajor( student.getMajor() );
        studentDTO.setPortfolioImage( student.getPortfolioImage() );
        studentDTO.setStudentCertificate( student.getStudentCertificate() );

        return studentDTO;
    }

    @Override
    public Student toEntity(StudentDTO studentDTO) {
        if ( studentDTO == null ) {
            return null;
        }

        Student student = new Student();

        student.setUser( mapStudentUserDTOToUser( studentDTO.getUser() ) );
        student.setUserId( studentDTO.getUserId() );
        student.setSchool( studentDTO.getSchool() );
        student.setMajor( studentDTO.getMajor() );
        student.setPortfolioImage( studentDTO.getPortfolioImage() );
        student.setStudentCertificate( studentDTO.getStudentCertificate() );

        return student;
    }
}
