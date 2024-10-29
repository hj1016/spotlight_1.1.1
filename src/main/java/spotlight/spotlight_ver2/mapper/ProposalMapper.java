package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.ProposalDTO;
import spotlight.spotlight_ver2.entity.Proposal;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;

@Mapper
public interface ProposalMapper {
    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    // Proposal -> ProposalDTO
    @Mapping(target = "recruiter", source = "recruiter", qualifiedByName = "mapRecruiterToDTO")
    @Mapping(target = "student", source = "student", qualifiedByName = "mapStudentToDTO")
    ProposalDTO toDTO(Proposal proposal);

    // ProposalDTO -> Proposal
    @Mapping(target = "recruiter", source = "recruiter", qualifiedByName = "mapDTOToRecruiter")
    @Mapping(target = "student", source = "student", qualifiedByName = "mapDTOToStudent")
    Proposal toEntity(ProposalDTO proposalDTO);

    // Recruiter -> ProposalRecruiterDTO
    @Named("mapRecruiterToDTO")
    default ProposalDTO.ProposalRecruiterDTO mapRecruiterToDTO(Recruiter recruiter) {
        if (recruiter == null) return null;
        ProposalDTO.ProposalRecruiterDTO recruiterDTO = new ProposalDTO.ProposalRecruiterDTO();
        recruiterDTO.setUserId(recruiter.getUserId());
        recruiterDTO.setCompany(recruiter.getCompany());
        recruiterDTO.setCertification(recruiter.getRecruiterCertificate());
        recruiterDTO.setUsername(recruiter.getUser() != null ? recruiter.getUser().getUsername() : null);
        return recruiterDTO;
    }

    // ProposalRecruiterDTO -> Recruiter
    @Named("mapDTOToRecruiter")
    default Recruiter mapDTOToRecruiter(ProposalDTO.ProposalRecruiterDTO recruiterDTO) {
        Recruiter recruiter = new Recruiter();
        recruiter.setUserId(recruiterDTO.getUserId());
        recruiter.setCompany(recruiterDTO.getCompany());
        recruiter.setRecruiterCertificate(recruiterDTO.getCertification());
        // `user`는 무시
        return recruiter;
    }

    // Student -> ProposalStudentDTO
    @Named("mapStudentToDTO")
    default ProposalDTO.ProposalStudentDTO mapStudentToDTO(Student student) {
        if (student == null) return null;
        ProposalDTO.ProposalStudentDTO studentDTO = new ProposalDTO.ProposalStudentDTO();
        studentDTO.setUserId(student.getUserId());
        studentDTO.setSchool(student.getSchool());
        studentDTO.setMajor(student.getMajor());
        studentDTO.setPortfolioImage(student.getPortfolioImage());
        return studentDTO;
    }

    // ProposalStudentDTO -> Student
    @Named("mapDTOToStudent")
    default Student mapDTOToStudent(ProposalDTO.ProposalStudentDTO studentDTO) {
        Student student = new Student();
        student.setUserId(studentDTO.getUserId());
        student.setSchool(studentDTO.getSchool());
        student.setMajor(studentDTO.getMajor());
        student.setPortfolioImage(studentDTO.getPortfolioImage());
        // `proposals`와 `projectRoles`는 무시
        return student;
    }
}