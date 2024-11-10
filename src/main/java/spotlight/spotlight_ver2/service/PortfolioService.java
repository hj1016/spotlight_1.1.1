package spotlight.spotlight_ver2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.StudentRepository;
import spotlight.spotlight_ver2.request.UploadPortfolioRequest;
import spotlight.spotlight_ver2.response.UploadPortfolioResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {
    private final Logger logger = LoggerFactory.getLogger(PortfolioService.class);
    private final StudentRepository studentRepository;
    private final UploadImageService uploadImageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PortfolioService(StudentRepository studentRepository, UploadImageService uploadImageService) {
        this.studentRepository = studentRepository;
        this.uploadImageService = uploadImageService;
    }

    @Transactional
    public UploadPortfolioResponse uploadPortfolio(User user, UploadPortfolioRequest request) {
        MultipartFile[] portfolioImages = request.getPortfolioImages();
        UploadPortfolioResponse response = new UploadPortfolioResponse();
        String joinedUrls = "";
        List<String> imageUrls = new ArrayList<>();
        long id = user.getId();

        if (portfolioImages.length > 0) {
            try {
                imageUrls = uploadImageService.uploadImages(portfolioImages);
                joinedUrls = String.join(",", imageUrls);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            String insertSql = "INSERT INTO portfolio (portfolio_images, user_id) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, joinedUrls, id);
            response.setSuccess(true);
            response.setPortfolioList(imageUrls);
            response.setMessage("포트폴리오 업로드 성공");
            return response;
        }
        response.setSuccess(false);
        response.setPortfolioList(null);
        response.setMessage("포트폴리오 업로드 실패");
        return response;
    }

    public Optional<Student> getStudentById(long id) {
        return studentRepository.findById(id);
    }
}
