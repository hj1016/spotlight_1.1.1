package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import spotlight.spotlight_ver2.exception.BadRequestException;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.response.SearchResponse;
import spotlight.spotlight_ver2.service.SearchSchoolMajorService;

import java.util.List;

@RestController
@Tag(name = "학교/학과 검색 API", description = "학교/학과 검색과 관련 API입니다.")
public class SearchSchoolMajorController {

    private final SearchSchoolMajorService searchSchoolMajorService;

    @Autowired
    public SearchSchoolMajorController(SearchSchoolMajorService searchSchoolMajorService) {
        this.searchSchoolMajorService = searchSchoolMajorService;
    }

    @Operation(
            summary = "학교/학과 검색",
            description = "OpenAPI를 활용하여 학교 또는 학과를 검색합니다. 학교와 학과 모두 제공되지 않은 경우 예외가 발생합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과 반환"),
            @ApiResponse(responseCode = "204", description = "검색 결과가 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "검색된 결과가 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/api/search/schoolandmajor")
    public ResponseEntity<SearchResponse> searchPosts(
            @RequestParam(required = true) String apiKey,
            @RequestParam(required = false) String school,
            @RequestParam(required = false) String major
    ) {
        try {
            // API 키 유효성 검증 (StringUtils를 사용하여 더 가독성 좋게 검증)
            if (!StringUtils.hasText(apiKey)) {
                throw new BadRequestException("API 키를 제공해야 합니다.");
            }

            // 학교와 학과 모두 제공되지 않은 경우 예외 처리
            if (school == null && major == null) {
                throw new BadRequestException("학교 또는 학과 이름을 반드시 입력해야 합니다.");
            }

            List<SchoolMajorDTO> results;

            if (school != null && major == null) {
                results = searchSchoolMajorService.searchSchools(apiKey, school);
            } else if (school == null && major != null) {
                results = searchSchoolMajorService.searchMajors(apiKey, major);
            } else {
                results = searchSchoolMajorService.searchBySchoolAndMajor(apiKey, school, major);
            }

            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new SearchResponse(true, "결과가 없습니다.", null));
            }

            return ResponseEntity.ok(new SearchResponse(true, "학교/학과 검색 결과입니다.", results));

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SearchResponse(false, "검색된 결과가 없습니다.", null));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SearchResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SearchResponse(false, "서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요.", null));
        }
    }
}