package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.exception.BadRequestException;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.UnauthorizedException;
import spotlight.spotlight_ver2.repository.FeedRepository;
import spotlight.spotlight_ver2.repository.ScrapRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;
import spotlight.spotlight_ver2.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final StudentRepository studentRepository;
    private final FeedRepository feedRepository;

    public ScrapService(ScrapRepository scrapRepository, StudentRepository studentRepository, FeedRepository feedRepository) {
        this.scrapRepository = scrapRepository;
        this.studentRepository = studentRepository;
        this.feedRepository = feedRepository;
    }

    public Map<String, Object> scrapFeed(Long feedId, User user, Stage stage, User scrappedUser) {
        // 게시물 검색
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("스크랩할 게시물을 찾을 수 없습니다."));

        // 이미 스크랩한 게시물인지 확인
        if (scrapRepository.existsByUserAndFeed(user, feed)) {
            throw new BadRequestException("이미 이 게시물을 스크랩했습니다.");
        }

        // feed, stage, scrappedUser 중 하나만 설정되도록 검증
        if ((feed != null && (stage != null || scrappedUser != null)) ||
                (stage != null && (feed != null || scrappedUser != null)) ||
                (scrappedUser != null && (feed != null || stage != null))) {
            throw new IllegalArgumentException("feed, stage, scrappedUser 중 하나만 설정해야 합니다.");
        }

        // Scrap 객체 생성 및 값 설정
        Scrap scrap = new Scrap();
        scrap.setUser(user);

        if (feed != null) {
            scrap.setFeed(feed);
        }
        if (stage != null) {
            scrap.setStageId(stage);
        }
        if (scrappedUser != null) {
            scrap.setScrappedUser(scrappedUser);
        }

        // Scrap 엔티티 저장
        scrapRepository.save(scrap);

        // 스크랩 수 증가
        feed.setScrap(feed.getScrap() + 1);
        feedRepository.save(feed);

        // 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "게시물이 성공적으로 스크랩되었습니다.");
        return response;
    }

    public Map<String, Object> unscrapFeed(Long feedId, User user, Stage stage, User scrappedUser) {
        // feed, stage, scrappedUser 중 하나만 설정되었는지 확인
        if ((feedId != null && (stage != null || scrappedUser != null)) ||
                (stage != null && (feedId != null || scrappedUser != null)) ||
                (scrappedUser != null && (feedId != null || stage != null))) {
            throw new IllegalArgumentException("feedId, stage, scrappedUser 중 하나만 설정해야 합니다.");
        }

        // 해당 조건에 맞는 스크랩 항목을 찾기
        Scrap scrap;
        if (feedId != null) {
            Feed feed = feedRepository.findById(feedId)
                    .orElseThrow(() -> new NotFoundException("스크랩을 취소할 게시물을 찾을 수 없습니다."));
            scrap = scrapRepository.findByUserAndFeed(user, feed)
                    .orElseThrow(() -> new BadRequestException("스크랩하지 않은 게시물입니다."));

            // 스크랩 수 감소
            feed.setScrap(feed.getScrap() - 1);
            feedRepository.save(feed);

        } else if (stage != null) {
            scrap = scrapRepository.findByUserAndStageId(user, stage)
                    .orElseThrow(() -> new BadRequestException("해당 단계의 스크랩 항목이 없습니다."));

        } else if (scrappedUser != null) {
            scrap = scrapRepository.findByUserAndScrappedUser(user, scrappedUser)
                    .orElseThrow(() -> new BadRequestException("스크랩하지 않은 사용자입니다."));
        } else {
            throw new IllegalArgumentException("스크랩을 취소할 항목이 지정되지 않았습니다.");
        }

        // 스크랩 항목 삭제
        scrapRepository.delete(scrap);

        // 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "스크랩이 성공적으로 취소되었습니다.");
        return response;
    }

    public void scrapStudent(Long userId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("학생을 스크랩하기 위해서는 RECRUITER 역할이 필요합니다.");
        }

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("스크랩할 학생을 찾을 수 없습니다."));

        boolean alreadyScrapped = scrapRepository.existsByUserAndScrappedUser(recruiter, student);
        if (alreadyScrapped) {
            throw new BadRequestException("이미 이 학생을 스크랩했습니다.");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(recruiter);
        scrap.setScrappedUser(student.getUser());
        scrapRepository.save(scrap);
    }

    public void unscrapStudent(Long userId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("학생을 스크랩 취소하기 위해서는 RECRUITER 역할이 필요합니다.");
        }

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("스크랩 취소할 학생을 찾을 수 없습니다."));

        Scrap scrap = scrapRepository.findByUserAndScrappedUser(recruiter, student.getUser())
                .orElseThrow(() -> new NotFoundException("해당 학생이 스크랩되어 있지 않습니다."));
        scrapRepository.delete(scrap);
    }
}