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
import spotlight.spotlight_ver2.response.ScrapResponse;

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

    public ScrapResponse scrapFeed(Long feedId, User currentUser) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("스크랩할 게시물을 찾을 수 없습니다."));

        if (scrapRepository.existsByUserAndFeed(currentUser, feed)) {
            throw new BadRequestException("이미 이 게시물을 스크랩했습니다.");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(currentUser);
        scrap.setFeed(feed);
        scrapRepository.save(scrap);

        // 스크랩 수 증가
        feed.setScrap(feed.getScrap() + 1);
        feedRepository.save(feed);

        return new ScrapResponse(true, "피드를 스크랩했습니다.", feedId, "Feed", feed.getScrap());
    }

    public ScrapResponse unscrapFeed(Long feedId, User currentUser) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("스크랩 취소할 게시물을 찾을 수 없습니다."));

        Scrap scrap = scrapRepository.findByUserAndFeed(currentUser, feed)
                .orElseThrow(() -> new NotFoundException("해당 게시물이 스크랩되어 있지 않습니다."));
        scrapRepository.delete(scrap);

        // 스크랩 수 감소
        feed.setScrap(feed.getScrap() - 1);
        feedRepository.save(feed);

        return new ScrapResponse(true, "피드 스크랩을 취소했습니다.", feedId, "Feed", feed.getScrap());
    }

    public boolean isStudentScrapped(Long feedId, Long studentId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("스크랩 상태를 확인하려면 RECRUITER만 가능합니다.");
        }

        // 학생 존재 여부 확인
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("학생을 찾을 수 없습니다."));

        // 현재 사용자가 해당 학생을 스크랩했는지 확인
        return scrapRepository.existsByUserAndScrappedUser(recruiter, student);
    }

    public ScrapResponse scrapStudent(Long studentId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("학생을 스크랩하기 위해서는 RECRUITER 역할이 필요합니다.");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("스크랩할 학생을 찾을 수 없습니다."));

        if (scrapRepository.existsByUserAndScrappedUser(recruiter, student)) {
            throw new BadRequestException("이미 이 학생을 스크랩했습니다.");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(recruiter);
        scrap.setScrappedUser(student.getUser());
        scrapRepository.save(scrap);

        return new ScrapResponse(true, "학생을 스크랩했습니다.", studentId, "Student", 0);
    }

    public ScrapResponse unscrapStudent(Long studentId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("학생을 스크랩 취소하기 위해서는 RECRUITER 역할이 필요합니다.");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("스크랩 취소할 학생을 찾을 수 없습니다."));

        Scrap scrap = scrapRepository.findByUserAndScrappedUser(recruiter, student.getUser())
                .orElseThrow(() -> new NotFoundException("해당 학생이 스크랩되어 있지 않습니다."));
        scrapRepository.delete(scrap);

        return new ScrapResponse(true, "학생 스크랩을 취소했습니다.", studentId, "Student", 0);
    }
}