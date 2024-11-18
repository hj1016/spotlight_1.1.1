package spotlight.spotlight_ver2.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import spotlight.spotlight_ver2.exception.ApiException;
import spotlight.spotlight_ver2.response.MajorDetailApiResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchSchoolMajorRepository {

    private final RestTemplate restTemplate;
    private final String careerNetApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SearchSchoolMajorRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.careerNetApiKey = "YOUR_API_KEY"; // API 키 직접 지정
    }

    // Open API 호출 (학교와 학과 검색)
    public List<SchoolMajorDTO> searchBySchoolAndMajor(String school, String major) {
        try {
            String apiUrl = buildApiUrl(school, major);
            ResponseEntity<byte[]> response = callApi(apiUrl);
            MajorDetailApiResponse apiResponse = parseResponse(response);
            return extractResults(apiResponse);
        } catch (Exception e) {
            System.err.println("Open API 호출 중 오류 발생: " + e.getMessage());
            return List.of();
        }
    }

    // Open API 호출 (학교 검색)
    public List<SchoolMajorDTO> searchBySchool(String school) {
        return searchBySchoolAndMajor(school, null);
    }

    // Open API 호출 (학과 검색)
    public List<SchoolMajorDTO> searchByMajor(String major) {
        return searchBySchoolAndMajor(null, major);
    }

    private String buildApiUrl(String school, String major) throws Exception {
        String apiUrl = "https://www.career.go.kr/cnet/openapi/getOpenApi.json" +
                "?apiKey=" + careerNetApiKey +
                "&svcType=api" +
                "&svcCode=MAJOR" +
                "&contentType=json" +
                "&gubun=대학교" +
                "&univSe=대학";

        if (school != null) {
            apiUrl += "&schoolName=" + URLEncoder.encode(school, StandardCharsets.UTF_8.toString());
        }
        if (major != null) {
            apiUrl += "&majorName=" + URLEncoder.encode(major, StandardCharsets.UTF_8.toString());
        }
        return apiUrl;
    }

    private ResponseEntity<byte[]> callApi(String apiUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "UTF-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(apiUrl, HttpMethod.GET, entity, byte[].class);
    }

    private MajorDetailApiResponse parseResponse(ResponseEntity<byte[]> response) throws Exception {
        String jsonResponse = new String(response.getBody(), StandardCharsets.UTF_8);
        return objectMapper.readValue(jsonResponse, MajorDetailApiResponse.class);
    }

    private List<SchoolMajorDTO> extractResults(MajorDetailApiResponse response) {
        if (response.getResult() != null && response.getResult().getContent() != null) {
            return response.getResult().getContent().stream()
                    .map(content -> new SchoolMajorDTO(content.getSchoolName(), content.getMajorName()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}