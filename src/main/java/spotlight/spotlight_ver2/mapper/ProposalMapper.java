package spotlight.spotlight_ver2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import spotlight.spotlight_ver2.dto.ProposalDTO;
import spotlight.spotlight_ver2.entity.Proposal;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.request.ProposalRequest;
import spotlight.spotlight_ver2.response.ProposalResponse;

@Mapper
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    // Proposal -> ProposalDTO
    @Mapping(target = "recruiter", source = "recruiter", qualifiedByName = "mapRecruiterToDTO")
    @Mapping(target = "student", source = "student", qualifiedByName = "mapStudentToDTO")
    ProposalDTO toDTO(Proposal proposal);

    // ProposalRequest -> Proposal
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "proposalId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Proposal toEntity(ProposalRequest request);

    // Proposal -> ProposalResponse
    @Mapping(target = "recruiter.certification", ignore = true)
    @Mapping(target = "recruiter.username", ignore = true)
    ProposalResponse toResponse(Proposal proposal);

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
}