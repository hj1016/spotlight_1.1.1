package spotlight.spotlight_ver2.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import spotlight.spotlight_ver2.exception.ApiException;
import spotlight.spotlight_ver2.response.MajorApiResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchSchoolMajorRepository {

    private static final Logger logger = LoggerFactory.getLogger(SearchSchoolMajorRepository.class);
    private final RestTemplate restTemplate;
    private final String careerNetApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SearchSchoolMajorRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        Dotenv dotenv = Dotenv.configure().load();
        this.careerNetApiKey = dotenv.get("API_KEY_CAREER");
    }

    // 공통 API 호출 메서드
    private MajorApiResponse callApi(String schoolQuery, String majorQuery) {
        String encodedSchoolQuery = "";
        String encodedMajorQuery = "";
        try {
            if (schoolQuery != null) {
                encodedSchoolQuery = URLEncoder.encode(schoolQuery, StandardCharsets.UTF_8.toString());
            }
            if (majorQuery != null) {
                encodedMajorQuery = URLEncoder.encode(majorQuery, StandardCharsets.UTF_8.toString());
            }
        } catch (UnsupportedEncodingException e) {
            throw new ApiException("파라미터 인코딩 중 오류 발생", e);
        }

        // 최종 API URL 구성
        String apiUrl = "https://www.career.go.kr/cnet/openapi/getOpenApi.json" +
                "?apiKey=" + careerNetApiKey +
                "&svcType=api" +
                "&svcCode=MAJOR" +
                "&contentType=json" +
                "&gubun=univ_list" +
                "&univSe=univ" +
                (encodedSchoolQuery.isEmpty() ? "" : "&school=" + encodedSchoolQuery) +
                (encodedMajorQuery.isEmpty() ? "" : "&major=" + encodedMajorQuery);

        logger.info("Encoded API URL: {}", apiUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "UTF-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            logger.info("API 호출 시작: URL = {}", apiUrl);

            // API 응답을 String으로 받아 UTF-8로 강제 변환
            ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, byte[].class);
            String jsonResponse = new String(response.getBody(), StandardCharsets.UTF_8);
            logger.info("UTF-8로 변환된 API 응답 내용: {}", jsonResponse);

            // JSON 문자열을 MajorApiResponse 객체로 변환
            return objectMapper.readValue(jsonResponse, MajorApiResponse.class);

        } catch (HttpClientErrorException e) {
            logger.error("API 호출 중 오류 발생: {}", e.getMessage());
            throw new ApiException("API 호출 실패: " + e.getMessage());
        } catch (Exception e) {
            logger.error("API 호출 중 예기치 않은 오류 발생: {}", e.getMessage());
            throw new ApiException("예기치 않은 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 학과 검색
    public List<SchoolMajorDTO> searchMajors(String majorQuery) {
        MajorApiResponse response = callApi(null, majorQuery);
        return response.getMajorList();
    }

    // 학교 검색
    public List<SchoolMajorDTO> searchSchools(String schoolQuery) {
        MajorApiResponse response = callApi(schoolQuery, null);
        return response.getMajorList();
    }

    // 학교와 학과로 검색
    public List<SchoolMajorDTO> searchBySchoolAndMajor(String schoolQuery, String majorQuery) {
        // 각 쿼리를 개별적으로 호출
        List<SchoolMajorDTO> schoolResults = searchSchools(schoolQuery);
        List<SchoolMajorDTO> majorResults = searchMajors(majorQuery);

        // 학교와 학과가 모두 일치하는 항목만 필터링
        return schoolResults.stream()
                .filter(school -> majorResults.stream()
                        .anyMatch(major -> major.getSchoolName() != null
                                && school.getSchoolName() != null
                                && major.getSchoolName().equals(school.getSchoolName())
                                && major.getMajorName().equalsIgnoreCase(majorQuery)))
                .collect(Collectors.toList());
    }
}