package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.ProposalDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.BadRequestException;
import spotlight.spotlight_ver2.exception.ForbiddenException;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.UnauthorizedException;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.service.FeedService;
import spotlight.spotlight_ver2.service.ProposalService;
import spotlight.spotlight_ver2.service.ScrapService;
import spotlight.spotlight_ver2.service.SearchHistoryService;

import java.util.Map;

@RestController
@RequestMapping("/api/proposals")
@Tag(name = "제안서 API", description = "인사 담당자가 관리하는 공고 제안서와 관련된 API입니다.")
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @Operation(summary = "새 제안서 생성", description = "제공된 세부정보로 새 제안서를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "제안서 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "제안서 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<?> createProposal(@RequestParam Long studentId, @RequestBody ProposalDTO proposalDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            ProposalDTO createdProposal = proposalService.createProposal(studentId, currentUser, proposalDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("제안서가 성공적으로 저장되었습니다.");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "기존 제안서 수정", description = "제안서 ID로 식별되는 기존 제안서를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "제안서 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "제안서 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{proposalId}")
    public ResponseEntity<?> updateProposal(@PathVariable Long proposalId, @RequestBody ProposalDTO proposalDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            ProposalDTO updatedProposal = proposalService.updateProposal(proposalId, currentUser, proposalDTO);

            return ResponseEntity.ok("제안서가 성공적으로 수정되었습니다.");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }
}