package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.dto.StudentDTO;
import spotlight.spotlight_ver2.entity.Stage;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.*;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.service.FeedService;
import spotlight.spotlight_ver2.service.ScrapService;
import spotlight.spotlight_ver2.service.SearchHistoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feeds")
@Tag(name = "피드 API", description = "피드와 관련된 API입니다.")
public class FeedController {
    @Autowired
    private final FeedService feedService;

    @Autowired
    private final SearchHistoryService searchHistoryService;

    @Autowired
    private final ScrapService scrapService;


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public FeedController(FeedService feedService, SearchHistoryService searchHistoryService, ScrapService scrapService, UserRepository userRepository) {
        this.feedService = feedService;
        this.searchHistoryService = searchHistoryService;
        this.scrapService = scrapService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "새 피드 생성", description = "새 피드 게시물을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "피드 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<?> createFeed(@RequestBody FeedDTO feedDTO) {
        try {
            FeedDTO createdFeed = feedService.createFeed(feedDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeed);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InternalServerErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "피드 조회", description = "주어진 피드 ID로 피드 게시물을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 조회 성공"),
            @ApiResponse(responseCode = "404", description = "피드 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{feedId}")
    public ResponseEntity<?> getFeed(@PathVariable Long feedId) {
        try {
            FeedDTO feed = feedService.getFeedById(feedId);
            return ResponseEntity.ok(feed);
        } catch (NotFoundException e) {
            return ErrorResponse.toResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        }
    }

    @Operation(summary = "피드 업데이트", description = "주어진 피드 ID의 피드를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "피드 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{feedId}")
    public ResponseEntity<?> updateFeed(@PathVariable Long feedId, @RequestBody FeedDTO feedDTO) {
        try {
            FeedDTO updatedFeed = feedService.updateFeed(feedId, feedDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedFeed);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InternalServerErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "피드 삭제", description = "주어진 피드 ID의 피드를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "피드 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{feedId}")
    public ResponseEntity<?> deleteFeed(@PathVariable Long feedId) {
        try {
            // 현재 인증된 사용자 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            // 피드 삭제 시 사용자 권한 검증
            feedService.deleteFeed(feedId, currentUser);

            return ResponseEntity.status(HttpStatus.OK).body("게시글이 삭제되었습니다.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (InternalServerErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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

    @Operation(summary = "피드 조회수 집계", description = "사용자별 피드 조회수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회수 집계 성공"),
            @ApiResponse(responseCode = "404", description = "피드를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{feedId}/hits")
    public ResponseEntity<?> incrementFeedHits(
            @PathVariable @Parameter(description = "피드 ID") Long feedId) {
        try {
            // 현재 인증된 사용자 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            FeedDTO feedDTO = feedService.incrementHits(feedId, currentUser);
            return ResponseEntity.ok(feedDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        }
    }

    @Operation(summary = "피드 스크랩", description = "피드를 스크랩합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스크랩 성공"),
            @ApiResponse(responseCode = "404", description = "피드를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "이미 스크랩된 피드"),
            @ApiResponse(responseCode = "401", description = "권한 없음")
    })
    @PostMapping("/{feedId}/scrap")
    public ResponseEntity<?> scrapFeed(
            @PathVariable Long feedId,
            @RequestParam Long stageId,
            @RequestParam Long scrappedUserId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            Stage stage = new Stage(); // stageId를 바탕으로 Stage 객체를 조회하는 로직을 추가해야 함
            stage.setId(stageId); // 간단하게 id 설정으로 처리

            User scrappedUser = userRepository.findById(scrappedUserId)
                    .orElseThrow(() -> new NotFoundException("스크랩된 사용자를 찾을 수 없습니다."));

            Map<String, Object> response = scrapService.scrapFeed(feedId, currentUser, stage, scrappedUser);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "피드 스크랩 취소", description = "피드의 스크랩을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스크랩 취소 성공"),
            @ApiResponse(responseCode = "404", description = "피드를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "스크랩되지 않은 피드"),
            @ApiResponse(responseCode = "401", description = "권한 없음")
    })
    @DeleteMapping("/{feedId}/scrap")
    public ResponseEntity<?> unscrapFeed(@PathVariable Long feedId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            Map<String, Object> response = scrapService.unscrapFeed(feedId, currentUser);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "프로젝트 팀원 조회", description = "특정 피드에 속한 팀원 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 팀원 목록을 조회했습니다."),
            @ApiResponse(responseCode = "404", description = "피드를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{feedId}/team-members")
    public ResponseEntity<?> getProjectTeamMembers(@PathVariable Long feedId) {
        try {
            // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // User currentUser = (User) authentication.getPrincipal();

            List<StudentDTO> teamMembers = feedService.getProjectTeamMembers(feedId);
            return ResponseEntity.ok(teamMembers);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "학생 스크랩", description = "학생을 스크랩합니다. 스크랩 기능은 RECRUITER 역할을 가진 사용자만 사용할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생을 성공적으로 스크랩했습니다."),
            @ApiResponse(responseCode = "401", description = "사용자가 인증되지 않았습니다."),
            @ApiResponse(responseCode = "400", description = "이미 스크랩한 학생입니다."),
            @ApiResponse(responseCode = "404", description = "학생을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{feedId}/students/{studentId}/scrap")
    public ResponseEntity<?> scrapStudent(@PathVariable Long feedId, @PathVariable Long studentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            // Feed 존재 여부 체크
            if (!feedService.existsById(feedId)) {
                throw new NotFoundException("스크랩할 학생을 찾을 수 없습니다.");
            }

            // 스크랩 기능 호출
            scrapService.scrapStudent(studentId, currentUser);

            return ResponseEntity.ok("학생을 스크랩했습니다.");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "학생 스크랩 취소", description = "학생 스크랩을 취소합니다. 스크랩 취소 기능은 RECRUITER 역할을 가진 사용자만 사용할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 스크랩을 성공적으로 취소했습니다."),
            @ApiResponse(responseCode = "401", description = "사용자가 인증되지 않았습니다."),
            @ApiResponse(responseCode = "400", description = "스크랩하지 않은 학생입니다."),
            @ApiResponse(responseCode = "404", description = "학생을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{feedId}/students/{studentId}/scrap")
    public ResponseEntity<?> unsrapStudent(@PathVariable Long feedId, @PathVariable Long studentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            // Feed 존재 여부 체크
            if (!feedService.existsById(feedId)) {
                throw new NotFoundException("스크랩할 게시물을 찾을 수 없습니다.");
            }

            // 스크랩 취소 기능 호출
            scrapService.unsrapStudent(studentId, currentUser);

            return ResponseEntity.ok("학생 스크랩을 취소했습니다.");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }
}