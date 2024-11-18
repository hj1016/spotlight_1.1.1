package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.repository.FeedRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FeedRepository feedRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, FeedRepository feedRepository) {
        this.studentRepository = studentRepository;
        this.feedRepository = feedRepository;
    }

    // 학생의 분야를 결정하는 메서드
    public Optional<Category> findMostRecentFeedCategory(Student student) {
        // Student의 최근 Feed 조회
        Optional<Feed> recentFeed = feedRepository.findTopByUserOrderByCreatedDateDesc(student.getUser());
        return recentFeed.map(Feed::getCategory); // Feed가 존재하면 Category 반환
    }

    public List<Student> getStudentsBySchool(String school) {
        return studentRepository.findBySchool(school);
    }

    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    public List<Student> getStudentsBySchoolAndMajor(String school, String major) {
        return studentRepository.findBySchoolAndMajor(school, major); }
}