package spotlight.spotlight_ver2.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.dto.ProposalDTO;
import spotlight.spotlight_ver2.entity.Proposal;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.ForbiddenException;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.UnauthorizedException;
import spotlight.spotlight_ver2.mapper.ProposalMapper;
import spotlight.spotlight_ver2.repository.ProposalRepository;
import spotlight.spotlight_ver2.repository.RecruiterRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final StudentRepository studentRepository;
    private final RecruiterRepository recruiterRepository;
    private final ProposalMapper proposalMapper = ProposalMapper.INSTANCE;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository, StudentRepository studentRepository, RecruiterRepository recruiterRepository) {
        this.proposalRepository = proposalRepository;
        this.studentRepository = studentRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @Transactional
    public ProposalDTO createProposal(Long studentId, User currentUser, ProposalDTO proposalDTO) {
        // 리크루터 확인
        Recruiter recruiter = recruiterRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ForbiddenException("리크루터 회원이 아닌 사용자는 공고 제안서를 작성할 수 없습니다."));

        // 학생 확인
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("학생을 찾을 수 없습니다."));

        // Proposal 엔티티 생성 및 설정
        Proposal proposal = proposalMapper.toEntity(proposalDTO);
        proposal.setRecruiter(recruiter);
        proposal.setStudent(student);
        proposal.setStatus("PENDING"); // 기본 상태 설정

        // Proposal 저장
        Proposal savedProposal = proposalRepository.save(proposal);

        return proposalMapper.toDTO(savedProposal);
    }

    @Transactional
    public ProposalDTO updateProposal(Long proposalId, User currentUser, ProposalDTO proposalDTO) {
        // 리크루터 확인
        Recruiter recruiter = recruiterRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ForbiddenException("리크루터 회원이 아닌 사용자는 공고 제안서를 작성할 수 없습니다."));

        // 제안서 존재 여부 확인
        Proposal existingProposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new NotFoundException("해당 제안서를 찾을 수 없습니다."));

        // 제안서 작성자가 현재 사용자인지 확인
        if (!existingProposal.getRecruiter().getUserId().equals(currentUser.getId())) {
            throw new UnauthorizedException("해당 제안서를 수정할 권한이 없습니다.");
        }

        // Proposal 수정
        // Proposal 수정
        existingProposal.setJob(proposalDTO.getJob() != null ? proposalDTO.getJob() : existingProposal.getJob());
        existingProposal.setContact(proposalDTO.getContact() != null ? proposalDTO.getContact() : existingProposal.getContact());
        existingProposal.setDescription(proposalDTO.getDescription() != null ? proposalDTO.getDescription() : existingProposal.getDescription());
        existingProposal.setStatus(proposalDTO.getStatus() != null ? proposalDTO.getStatus() : existingProposal.getStatus());

        // 수정된 Proposal 저장
        Proposal updatedProposal = proposalRepository.save(existingProposal);

        return proposalMapper.toDTO(updatedProposal);
    }
}