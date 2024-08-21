package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.ProjectRole;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    // User와 accepted가 false인 프로젝트 역할 리스트를 찾는 쿼리
    List<ProjectRole> findByStudentAndAcceptedFalse(User student);
}