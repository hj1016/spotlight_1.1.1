package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spotlight.spotlight_ver2.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // 학교만으로 학생 검색
    @Query("SELECT s FROM Student s WHERE s.school = :school")
    List<Student> findBySchool(@Param("school") String school);

    // 학과만으로 학생 검색
    @Query("SELECT s FROM Student s WHERE s.major = :major")
    List<Student> findByMajor(@Param("major") String major);

    // 학교와 학과로 학생 검색
    @Query("SELECT s FROM Student s WHERE s.school = :school AND s.major = :major")
    List<Student> findBySchoolAndMajor(@Param("school") String school, @Param("major") String major);
}