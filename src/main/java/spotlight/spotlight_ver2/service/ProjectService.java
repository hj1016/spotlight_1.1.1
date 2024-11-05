package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.exception.*;
import spotlight.spotlight_ver2.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;
    private final FeedRepository feedRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository,
                          StudentRepository studentRepository, NotificationRepository notificationRepository, FeedRepository feedRepository) {
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.studentRepository = studentRepository;
        this.notificationRepository = notificationRepository;
        this.feedRepository = feedRepository;
    }

    // 특정 학생이 수락하지 않은 초대장을 찾는 메서드
    public List<ProjectRole> getPendingInvitations(User student) {
        return projectRoleRepository.findByStudentAndAcceptedFalse(student);
    }

    public ProjectRole inviteTeamMember(Long projectId, Long studentId, String role) {
        // 프로젝트를 ID로 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. ID: " + projectId));

        // 학생을 ID로 조회
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("학생을 찾을 수 없습니다. ID: " + studentId));

        // 새로운 ProjectRole 객체 생성
        ProjectRole projectRole = new ProjectRole();
        projectRole.setProject(project);
        projectRole.setStudent(student);
        projectRole.setRole(role);
        projectRole.setAccepted(false);

        // ProjectRole 저장
        projectRoleRepository.save(projectRole);

        // 초대 알림 생성 및 저장 (임시: Notification Refactoring 후 다시 수정 필요)
        Notification notification = new Notification();
        notification.setType("INVITATION"); // 초대 타입
        notification.setStatus("UNREAD"); // 읽지 않음 상태
        notification.setMessage("프로젝트 '" + project.getName() + "'에 초대되었습니다.");
        notification.setSender(project.getCreator()); // 프로젝트 생성자
        notification.setReceiver(student.getUser()); // 초대받는 학생
        notificationRepository.save(notification);

        return projectRole;
    }

    public void acceptInvitation(Long projectRoleId) {
        // ProjectRole을 ID로 조회
        ProjectRole projectRole = projectRoleRepository.findById(projectRoleId)
                .orElseThrow(() -> new NotFoundException("ProjectRole을 찾을 수 없습니다. ID: " + projectRoleId));

        // 초대 수락 상태로 변경
        projectRole.setAccepted(true);
        projectRoleRepository.save(projectRole);

        // 수락 알림 생성 및 저장 (임시: Notification Refactoring 후 다시 수정 필요)
        Notification notification = new Notification();
        notification.setType("ACCEPTANCE"); // 수락 타입
        notification.setStatus("UNREAD"); // 읽지 않음 상태
        notification.setMessage("프로젝트 '" + projectRole.getProject().getName() + "'에 대한 초대가 수락되었습니다.");
        notification.setSender(projectRole.getStudent().getUser()); // 초대를 수락한 학생
        notification.setReceiver(projectRole.getProject().getCreator()); // 프로젝트 생성자
        notificationRepository.save(notification);
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