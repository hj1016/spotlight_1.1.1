package spotlight.spotlight_ver2.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final StudentRepository studentRepository;
    private final RecruiterRepository recruiterRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ProposalMapper proposalMapper = ProposalMapper.INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(ProposalService.class);

    @Autowired
    public ProposalService(ProposalRepository proposalRepository, StudentRepository studentRepository, RecruiterRepository recruiterRepository, NotificationService notificationService, UserService userService) {
        this.proposalRepository = proposalRepository;
        this.studentRepository = studentRepository;
        this.recruiterRepository = recruiterRepository;
        this.notificationService = notificationService;
        this.userService = userService;
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

    // 공고 제안서 목록 조회 (학생)
    public List<ProposalResponse> getProposalsByStudent(long id) {
        User user = userService.getUserById(id);
        List<Proposal> proposals = proposalRepository.findByStudent_User(user);
        logger.info("Proposals: {}", proposals);
        proposals.forEach(proposal -> logger.info("Proposal: {}", proposal));

        return proposals.stream()
                .map(this::toProposalResponse)
                .collect(Collectors.toList());
    }

    // 공고 제안서 목록 조회 (리크루터)
    public List<ProposalResponse> getProposalsByRecruiter(long id) {
        User user = userService.getUserById(id);
        List<Proposal> proposals = proposalRepository.findByRecruiter_User(user);

        return proposals.stream()
                .map(this::toProposalResponse)
                .collect(Collectors.toList());
    }

    // 공고 제안서 세부 내용 조회 (학생/리크루터)
    public ProposalResponse getProposalDetails(long id, Long proposalId, boolean isStudent) {
        Proposal proposal;

        if (isStudent) {
            proposal = proposalRepository.findByProposalIdAndStudent_UserId(proposalId, id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 제안서가 없습니다."));
        } else {
            proposal = proposalRepository.findByProposalIdAndRecruiter_UserId(proposalId, id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 제안서가 없습니다."));
        }
        return toProposalResponse(proposal);
    }

    private ProposalResponse toProposalResponse(Proposal proposal) {
        ProposalResponse response = new ProposalResponse();
        response.setProposalId(proposal.getProposalId());
        response.setJob(proposal.getJob());
        response.setContact(proposal.getContact());
        response.setDescription(proposal.getDescription());
        response.setCreatedDate(proposal.getCreatedDate().toString());
        response.setStatus(proposal.getStatus());

        Long recruiterUserId = proposal.getRecruiter().getUserId();
        Recruiter recruiter = recruiterRepository.findByUserId(recruiterUserId).orElse(null);
        ProposalDTO.ProposalRecruiterDTO recruiterDTO = new ProposalDTO.ProposalRecruiterDTO();
        if (recruiter != null) {
            recruiterDTO.setUserId(recruiter.getUserId());
            recruiterDTO.setCompany(recruiter.getCompany());
            recruiterDTO.setCertification(recruiter.getRecruiterCertificate());
            recruiterDTO.setUsername(recruiter.getUser().getUsername());
        }
        response.setRecruiter(recruiterDTO);

        Long studentUserId = proposal.getStudent().getUserId();
        Student student = studentRepository.findByUserId(studentUserId).orElse(null);
        ProposalDTO.ProposalStudentDTO studentDTO = new ProposalDTO.ProposalStudentDTO();
        if (student != null) {
            studentDTO.setUserId(student.getUserId());
            studentDTO.setMajor(student.getMajor());
            studentDTO.setPortfolioImage(student.getPortfolioImage());
            studentDTO.setSchool(student.getSchool());
        }
        response.setStudent(studentDTO);

        return response;
    }
}