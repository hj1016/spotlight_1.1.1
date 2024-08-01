package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
