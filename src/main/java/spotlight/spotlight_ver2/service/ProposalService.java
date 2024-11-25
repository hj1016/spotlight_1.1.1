package spotlight.spotlight_ver2.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.config.NotificationConstants;
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
import spotlight.spotlight_ver2.request.ProposalRequest;
import spotlight.spotlight_ver2.response.ProposalResponse;

import java.time.LocalDateTime;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final StudentRepository studentRepository;
    private final RecruiterRepository recruiterRepository;
    private final NotificationService notificationService;
    private final ProposalMapper proposalMapper = ProposalMapper.INSTANCE;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository, StudentRepository studentRepository, RecruiterRepository recruiterRepository, NotificationService notificationService) {
        this.proposalRepository = proposalRepository;
        this.studentRepository = studentRepository;
        this.recruiterRepository = recruiterRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ProposalResponse createProposal(ProposalRequest proposalRequest, User currentUser) {
        Recruiter recruiter = recruiterRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException("리쿠르터 권한이 필요합니다."));

        Student student = studentRepository.findById(proposalRequest.getStudentId())
                .orElseThrow(() -> new NotFoundException("학생을 찾을 수 없습니다."));

        Proposal proposal = proposalMapper.toEntity(proposalRequest);
        proposal.setRecruiter(recruiter);
        proposal.setStudent(student);
        proposal.setStatus("PENDING");
        proposal.setCreatedDate(LocalDateTime.now());

        Proposal savedProposal = proposalRepository.save(proposal);

        String notificationMessage = recruiter.getUser().getName() + "님이 새로운 제안서를 보냈습니다.";
        notificationService.createNotification(recruiter.getUser(), student.getUser(), NotificationConstants.TYPE_NEW_PROPOSAL, notificationMessage);

        return proposalMapper.toResponse(savedProposal);
    }

    @Transactional
    public ProposalResponse updateProposal(Long proposalId, ProposalRequest proposalRequest, User currentUser) {
        // Proposal 조회
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new NotFoundException("제안서를 찾을 수 없습니다."));

        // 현재 사용자가 해당 Proposal의 리쿠르터인지 확인
        if (!proposal.getRecruiter().getUserId().equals(currentUser.getId())) {
            throw new UnauthorizedException("이 제안서를 수정할 권한이 없습니다.");
        }

        // 요청 데이터로 Proposal 필드 업데이트
        proposal.setJob(proposalRequest.getJob() != null ? proposalRequest.getJob() : proposal.getJob());
        proposal.setContact(proposalRequest.getContact() != null ? proposalRequest.getContact() : proposal.getContact());
        proposal.setDescription(proposalRequest.getDescription() != null ? proposalRequest.getDescription() : proposal.getDescription());
        proposal.setStatus("UPDATED");

        // Proposal 저장
        Proposal updatedProposal = proposalRepository.save(proposal);

        // Proposal -> ProposalResponse 매핑
        return proposalMapper.toResponse(updatedProposal);
    }
}