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
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public ScrapService(ScrapRepository scrapRepository, StudentRepository studentRepository, UserRepository userRepository, FeedRepository feedRepository) {
        this.scrapRepository = scrapRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
    }

    public Map<String, Object> scrapFeed(Long feedId, User user, Stage stage, User scrappedUser) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("스크랩할 게시물을 찾을 수 없습니다."));

        if (scrapRepository.existsByUserAndFeed(user, feed)) {
            throw new BadRequestException("이미 이 게시물을 스크랩했습니다.");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(user);
        scrap.setFeed(feed);
        scrap.setStageId(stage);
        scrap.setScrappedUser(scrappedUser);
        scrapRepository.save(scrap);

        feed.setScrap(feed.getScrap() + 1); // 스크랩 수 증가
        feedRepository.save(feed);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "게시물이 성공적으로 스크랩되었습니다.");
        return response;
    }

    public Map<String, Object> unscrapFeed(Long feedId, User user) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException("스크랩을 취소할 게시물을 찾을 수 없습니다."));

        Scrap scrap = scrapRepository.findByUserAndFeed(user, feed)
                .orElseThrow(() -> new BadRequestException("스크랩하지 않은 게시물입니다."));

        scrapRepository.delete(scrap);

        feed.setScrap(feed.getScrap() - 1); // 스크랩 수 감소
        feedRepository.save(feed);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "게시물 스크랩이 성공적으로 취소되었습니다.");
        return response;
    }

    public void scrapStudent(Long studentId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("학생을 스크랩하기 위해서는 RECRUITER 역할이 필요합니다.");
        }

        Student student = studentRepository.findById(studentId)
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

    public void unsrapStudent(Long studentId, User recruiter) {
        if (!recruiter.getRole().equals(Role.RECRUITER)) {
            throw new UnauthorizedException("학생을 스크랩 취소하기 위해서는 RECRUITER 역할이 필요합니다.");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("스크랩 취소할 학생을 찾을 수 없습니다."));

        Scrap scrap = scrapRepository.findByUserAndScrappedUser(recruiter, student)
                .orElseThrow(() -> new NotFoundException("해당 학생이 스크랩되어 있지 않습니다."));
        scrapRepository.delete(scrap);
    }
}