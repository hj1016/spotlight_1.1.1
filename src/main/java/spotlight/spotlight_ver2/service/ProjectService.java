package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.exception.*;
import spotlight.spotlight_ver2.repository.*;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository,
                          StudentRepository studentRepository, NotificationRepository notificationRepository) {
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.studentRepository = studentRepository;
        this.notificationRepository = notificationRepository;
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
}