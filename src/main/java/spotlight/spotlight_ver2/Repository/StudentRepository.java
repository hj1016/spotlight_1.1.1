package spotlight.spotlight_ver2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.Entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
