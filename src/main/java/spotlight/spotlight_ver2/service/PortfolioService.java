package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
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
    private final StudentRepository studentRepository;
    private final UploadImageService uploadImageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PortfolioService(StudentRepository studentRepository, UploadImageService uploadImageService, UserService userService) {
        this.studentRepository = studentRepository;
        this.uploadImageService = uploadImageService;
    }

    public UploadPortfolioResponse uploadPortfolio(User user, UploadPortfolioRequest request) {
        MultipartFile[] portfolioImages = request.getPortfolioImages();
        UploadPortfolioResponse response = new UploadPortfolioResponse();
        String joinedUrls;
        List<String> imageUrls = new ArrayList<>();
        long id = user.getId();

        if (portfolioImages != null && portfolioImages.length > 0) {
            try {
                imageUrls = uploadImageService.uploadImages(portfolioImages);
                joinedUrls = String.join(",", imageUrls);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String sql = "UPDATE portfolio SET portfolio_images = ? WHERE id = ?";
            jdbcTemplate.update(sql, joinedUrls, id);
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
