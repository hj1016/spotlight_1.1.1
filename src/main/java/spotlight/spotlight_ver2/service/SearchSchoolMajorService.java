package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.mapper.FeedMapper;
import spotlight.spotlight_ver2.repository.FeedRepository;
import spotlight.spotlight_ver2.repository.SearchSchoolMajorRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchSchoolMajorService {
    private final SearchSchoolMajorRepository searchSchoolMajorRepository;
    private final StudentRepository studentRepository;
    private final FeedRepository feedRepository;
    private final FeedMapper feedMapper = FeedMapper.INSTANCE;

    @Autowired
    public SearchSchoolMajorService(SearchSchoolMajorRepository searchSchoolMajorRepository, StudentRepository studentRepository, FeedRepository feedRepository) {
        this.searchSchoolMajorRepository = searchSchoolMajorRepository;
        this.studentRepository = studentRepository;
        this.feedRepository = feedRepository;
    }

    // 학교로 검색
    public List<FeedDTO> searchFeedsBySchool(String school) {
        // OpenAPI 호출
        List<SchoolMajorDTO> results = searchSchoolMajorRepository.searchBySchool(school);

        // OpenAPI 결과가 없으면 DB 조회
        if (results.isEmpty()) {
            results = studentRepository.findBySchool(school).stream()
                    .map(student -> new SchoolMajorDTO(student.getSchool(), student.getMajor()))
                    .collect(Collectors.toList());
        }

        if (results.isEmpty()) {
            throw new NotFoundException("해당 학교의 학생이 작성한 피드를 찾을 수 없습니다.");
        }

        // 결과에서 학생 정보를 가져와 FeedDTO 리스트 반환
        List<Student> students = studentRepository.findBySchool(school);

        if (students.isEmpty()) {
            throw new NotFoundException("해당 학교의 학생이 없습니다.");
        }

        List<Feed> feeds = feedRepository.findByStudentIdIn(
                students.stream().map(Student::getUserId).collect(Collectors.toList())
        );

        if (feeds.isEmpty()) {
            throw new NotFoundException("학생들이 작성한 피드를 찾을 수 없습니다.");
        }

        return feeds.stream()
                .map(feedMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 학과로 검색
    public List<FeedDTO> searchFeedsByMajor(String major) {
        List<SchoolMajorDTO> results = searchSchoolMajorRepository.searchByMajor(major);

        if (results.isEmpty()) {
            results = studentRepository.findByMajor(major).stream()
                    .map(student -> new SchoolMajorDTO(student.getSchool(), student.getMajor()))
                    .collect(Collectors.toList());
        }

        if (results.isEmpty()) {
            throw new NotFoundException("해당 학과의 학생이 작성한 피드를 찾을 수 없습니다.");
        }

        List<Student> students = studentRepository.findByMajor(major);

        if (students.isEmpty()) {
            throw new NotFoundException("해당 학과의 학생이 없습니다.");
        }

        List<Feed> feeds = feedRepository.findByStudentIdIn(
                students.stream().map(Student::getUserId).collect(Collectors.toList())
        );

        if (feeds.isEmpty()) {
            throw new NotFoundException("학생들이 작성한 피드를 찾을 수 없습니다.");
        }

        return feeds.stream()
                .map(feedMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 학교와 학과로 검색
    public List<FeedDTO> searchFeedsBySchoolAndMajor(String school, String major) {
        List<SchoolMajorDTO> apiResults = searchSchoolMajorRepository.searchBySchoolAndMajor(school, major);

        List<Student> students;
        if (apiResults.isEmpty()) {
            students = studentRepository.findBySchoolAndMajor(school, major);
        } else {
            students = studentRepository.findBySchoolAndMajor(
                    apiResults.get(0).getSchoolName(), apiResults.get(0).getMajorName()
            );
        }

        if (students.isEmpty()) {
            throw new NotFoundException("해당 학과의 학생이 작성한 프로젝트가 없습니다.");
        }

        List<Feed> feeds = feedRepository.findByStudentIdIn(
                students.stream().map(Student::getUserId).collect(Collectors.toList())
        );

        if (feeds.isEmpty()) {
            throw new NotFoundException("학생들의 프로젝트를 찾을 수 없습니다.");
        }

        return feeds.stream()
                .map(feedMapper::toDTO)
                .collect(Collectors.toList());
    }
}