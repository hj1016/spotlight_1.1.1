package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}