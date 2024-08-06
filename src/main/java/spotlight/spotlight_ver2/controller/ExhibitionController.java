package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.DTO.ExhibitionDTO;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.response.ExhibitionResponse;
import spotlight.spotlight_ver2.service.ExhibitionService;

@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    /**
     * 전시 정보 생성
     *
     * @param exhibitionDTO 전시 정보 DTO
     * @return 생성된 전시 정보 응답
     */
    @PostMapping(consumes = "application/json", produces = "application/json; charset=UTF-8")
    @Operation(summary = "createExhibition", description = "전시 정보를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "전시 정보가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    public ResponseEntity<?> createExhibition(@Parameter(description = "전시 정보를 담고 있는 DTO 객체") @RequestBody ExhibitionDTO exhibitionDTO) {
        try {
            ExhibitionDTO createdExhibition = exhibitionService.createExhibition(exhibitionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ExhibitionResponse(true, createdExhibition));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("전시 정보를 저장하는 동안 오류가 발생했습니다. 다시 시도해주세요."));
        }
    }
}