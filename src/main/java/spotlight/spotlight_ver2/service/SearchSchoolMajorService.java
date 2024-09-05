package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.repository.SearchSchoolMajorRepository;

import java.util.List;

@Service
public class SearchSchoolMajorService {
    private final SearchSchoolMajorRepository searchSchoolMajorRepository;

    @Autowired
    public SearchSchoolMajorService(SearchSchoolMajorRepository searchSchoolMajorRepository) {
        this.searchSchoolMajorRepository = searchSchoolMajorRepository;
    }

    // 학교 이름으로 검색
    public List<SchoolMajorDTO> searchSchools(String apiKey, String schoolQuery) {
        List<SchoolMajorDTO> results = searchSchoolMajorRepository.searchSchools(apiKey, schoolQuery);
        if (results.isEmpty()) {
            throw new NotFoundException("해당 학교에 대한 검색 결과가 없습니다.");
        }
        return results;
    }

    // 학과 이름으로 검색
    public List<SchoolMajorDTO> searchMajors(String apiKey, String majorQuery) {
        List<SchoolMajorDTO> results = searchSchoolMajorRepository.searchMajors(apiKey, majorQuery);
        if (results.isEmpty()) {
            throw new NotFoundException("해당 학과에 대한 검색 결과가 없습니다.");
        }
        return results;
    }

    // 학교와 학과로 검색
    public List<SchoolMajorDTO> searchBySchoolAndMajor(String apiKey, String schoolQuery, String majorQuery) {
        List<SchoolMajorDTO> results = searchSchoolMajorRepository.searchBySchoolAndMajor(apiKey, schoolQuery, majorQuery);
        if (results.isEmpty()) {
            throw new NotFoundException("해당 학교와 학과에 대한 검색 결과가 없습니다.");
        }
        return results;
    }
}
