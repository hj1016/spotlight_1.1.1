package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spotlight.spotlight_ver2.entity.Recruiter;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
}
