package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.service.FeedService;
import spotlight.spotlight_ver2.service.SearchHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@Tag(name = "피드 API", description = "피드와 관련된 API입니다.")
public class FeedController {

    private final FeedService feedService;
    private final SearchHistoryService searchHistoryService;

    @Autowired
    public FeedController(FeedService feedService, SearchHistoryService searchHistoryService) {
        this.feedService = feedService;
        this.searchHistoryService = searchHistoryService;
    }

    @Operation(summary = "해시태그 검색", description = "해시태그를 통해 피드를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 검색 성공"),
            @ApiResponse(responseCode = "404", description = "해시태그를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchFeeds(@RequestParam String hashtag) {
        try {
            List<FeedDTO> feeds = feedService.searchFeedsByHashtag(hashtag);
            return new ResponseEntity<>(feeds, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "해시태그 검색 이력", description = "사용자의 검색 기록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 기록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/search/history")
    public ResponseEntity<?> getSearchHistory() {
        try {
            List<String> history = searchHistoryService.getSearchHistory();
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "해시태그 검색 이력 기반 조회", description = "이전에 검색한 기록을 통해 피드를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 검색 성공"),
            @ApiResponse(responseCode = "404", description = "해시태그를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/search/history/{hashtag}")
    public ResponseEntity<?> searchByHistory(@PathVariable String hashtag) {
        try {
            List<FeedDTO> feeds = feedService.searchFeedsByHashtag(hashtag);
            return new ResponseEntity<>(feeds, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}