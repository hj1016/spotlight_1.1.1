package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.response.ExhibitionResponse;
import spotlight.spotlight_ver2.exception.UserNotFoundException;
import spotlight.spotlight_ver2.exception.InternalServerErrorException;
import spotlight.spotlight_ver2.service.ExhibitionService;

@Tag(name="전시 정보 API", description = "프로젝트의 전시 정보를 입력받는 API 입니다.")
@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json; charset=UTF-8")
    @Operation(summary = "전시 정보 입력", description = "전시 정보를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "전시 정보가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    public ResponseEntity<?> createExhibition(@Parameter(description = "전시 정보를 담고 있는 DTO 객체") @RequestBody ExhibitionDTO exhibitionDTO) {
        try {
            ExhibitionDTO createdExhibition = exhibitionService.createExhibition(exhibitionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ExhibitionResponse(true, createdExhibition));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (InternalServerErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("전시 정보를 저장하는 동안 오류가 발생했습니다. 다시 시도해주세요."));
        }
    }
}