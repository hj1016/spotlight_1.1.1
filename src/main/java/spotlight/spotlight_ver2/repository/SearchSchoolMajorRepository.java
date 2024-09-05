package spotlight.spotlight_ver2.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import spotlight.spotlight_ver2.exception.ApiException;
import spotlight.spotlight_ver2.response.MajorApiResponse;

import java.util.List;

@Repository
public class SearchSchoolMajorRepository {

    private static final Logger logger = LoggerFactory.getLogger(SearchSchoolMajorRepository.class);
    private final RestTemplate restTemplate;

    public SearchSchoolMajorRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 공통 API 호출 메서드
    private MajorApiResponse callApi(String apiKey, String query) {
        String apiUrl = "https://www.career.go.kr/cnet/front/openapi/openApiMajorCenter.do?key=" + apiKey + "&major=" + query;

        try {
            logger.info("API 호출 시작: URL = {}", apiUrl);  // API 호출 시작 로깅
            MajorApiResponse response = restTemplate.getForObject(apiUrl, MajorApiResponse.class);
            logApiResponse(response);  // 응답 데이터 로깅
            if (response == null || response.getMajorList() == null) {
                throw new ApiException("API로부터 유효한 데이터를 받지 못했습니다.");
            }
            return response;
        } catch (Exception e) {
            logger.error("API 호출 중 오류 발생: {}", e.getMessage());  // 오류 발생 시 에러 로깅
            throw new ApiException("예기치 않은 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // API 응답 데이터 로깅 메서드
    private void logApiResponse(MajorApiResponse response) {
        if (response != null && response.getMajorList() != null) {
            logger.info("API 응답 데이터 수신: 총 {}개의 학과 데이터", response.getMajorList().size());
            // 학과 데이터 일부만 출력 (처음 3개의 데이터만)
            response.getMajorList().stream()
                    .limit(3)
                    .forEach(dto -> logger.info("학교: {}, 학과: {}", dto.getSchoolName(), dto.getMajorName()));
        } else {
            logger.warn("API 응답 데이터가 없습니다.");  // 응답 데이터가 없을 경우 경고 로그
        }
    }

    // 학과 검색
    public List<SchoolMajorDTO> searchMajors(String apiKey, String majorQuery) {
        MajorApiResponse response = callApi(apiKey, majorQuery);
        return response.getMajorList();
    }

    // 학교 검색
    public List<SchoolMajorDTO> searchSchools(String apiKey, String schoolQuery) {
        MajorApiResponse response = callApi(apiKey, schoolQuery);
        return response.getMajorList();
    }

    // 학교와 학과로 검색
    public List<SchoolMajorDTO> searchBySchoolAndMajor(String apiKey, String schoolQuery, String majorQuery) {
        MajorApiResponse response = callApi(apiKey, majorQuery);
        return response.getMajorList().stream()
                .filter(major -> major.getSchoolName().equalsIgnoreCase(schoolQuery))
                .toList();
    }
}