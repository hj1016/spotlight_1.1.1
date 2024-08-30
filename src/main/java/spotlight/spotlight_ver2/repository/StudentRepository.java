package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spotlight.spotlight_ver2.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
