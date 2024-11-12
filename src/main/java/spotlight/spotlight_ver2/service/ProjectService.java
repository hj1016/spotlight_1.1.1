package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.exception.*;
import spotlight.spotlight_ver2.exception.IllegalAccessException;
import spotlight.spotlight_ver2.mapper.NotificationResponseMapper;
import spotlight.spotlight_ver2.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final StudentRepository studentRepository;
    private final FeedRepository feedRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final NotificationResponseMapper notificationResponseMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository, StudentRepository studentRepository, FeedRepository feedRepository, NotificationRepository notificationRepository, NotificationService notificationService, NotificationResponseMapper notificationResponseMapper) {
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.studentRepository = studentRepository;
        this.feedRepository = feedRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.notificationResponseMapper = notificationResponseMapper;
    }

    // 특정 학생이 수락하지 않은 초대장을 찾는 메서드
    public List<ProjectRole> getPendingInvitations(User student) {
        return projectRoleRepository.findByStudentAndAcceptedFalse(student);
    }

    public ProjectRole inviteTeamMember(Long projectId, Long userId, String role) {
        // 프로젝트를 ID로 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. ID: " + projectId));

        // 사용자 ID로 학생을 조회
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자 ID로 학생을 찾을 수 없습니다. ID: " + userId));

        // 새로운 ProjectRole 객체 생성
        ProjectRole projectRole = new ProjectRole();
        projectRole.setProject(project);
        projectRole.setStudent(student);
        projectRole.setRole(role);
        projectRole.setAccepted(false);

        // ProjectRole 저장
        projectRoleRepository.save(projectRole);

        notificationService.createNotification(
                project.getCreator(),
                student.getUser(),
                "초대",
                "프로젝트 '" + project.getName() + "'에 초대되었습니다."
        );

        return projectRole;
    }

    public void acceptInvitation(Long projectRoleId, Long userId) {
        // ProjectRole을 ID로 조회
        ProjectRole projectRole = projectRoleRepository.findById(projectRoleId)
                .orElseThrow(() -> new NotFoundException("ProjectRole을 찾을 수 없습니다. ID: " + projectRoleId));

        // 초대를 받은 사용자인지 확인
        if (!projectRole.getStudent().getUser().getId().equals(userId)) {
            throw new IllegalAccessException("해당 초대를 수락할 권한이 없습니다.");
        }

        // 초대 수락 상태로 변경
        projectRole.setAccepted(true);
        projectRoleRepository.save(projectRole);

        notificationService.createNotification(
                projectRole.getStudent().getUser(),
                projectRole.getProject().getCreator(),
                "수락",
                "프로젝트 '" + projectRole.getProject().getName() + "'에 대한 초대가 수락되었습니다."
        );
    }

    // 키워드를 기반으로 프로젝트를 검색
    public List<Project> searchProjectsByKeyword(String keyword) {
        List<Feed> feeds = feedRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return feeds.stream()
                .map(Feed::getProject) // Feed에서 Project 정보 추출
                .distinct() // 중복 제거
                .limit(3) // 일단 3개만 추천
                .collect(Collectors.toList());
    }

    // 특정 카테고리에 속하는 상위 3개의 프로젝트 검색
    public List<Project> searchProjectsByCategory(Category category) {
        List<Feed> feeds = feedRepository.findByCategory(category);
        return feeds.stream()
                .map(Feed::getProject) // Feed에서 Project 정보 추출
                .distinct()
                .limit(3)  // 상위 3개로 제한
                .collect(Collectors.toList());
    }

    // 특정 해시태그가 포함된 프로젝트 검색
    public List<Project> searchProjectsByHashtag(String hashtag) {
        List<Feed> feeds = feedRepository.findByHashtagsHashtag(hashtag);
        return feeds.stream()
                .map(Feed::getProject)
                .distinct()
                .limit(3)
                .collect(Collectors.toList());
    }
}