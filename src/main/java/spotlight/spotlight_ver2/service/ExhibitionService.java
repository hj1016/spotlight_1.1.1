package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.InternalServerErrorException;
import spotlight.spotlight_ver2.mapper.ExhibitionMapper;
import spotlight.spotlight_ver2.repository.ExhibitionRepository;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.request.ExhibitionRequest;
import spotlight.spotlight_ver2.response.ExhibitionResponse;

@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final UserRepository userRepository;
    private final ExhibitionMapper exhibitionMapper = ExhibitionMapper.INSTANCE;

    public ExhibitionService(ExhibitionRepository exhibitionRepository, UserRepository userRepository) {
        this.exhibitionRepository = exhibitionRepository;
        this.userRepository = userRepository;
    }

    public ExhibitionResponse createExhibition(ExhibitionRequest exhibitionRequest) {
        User user = userRepository.findById(exhibitionRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // Exhibition 엔티티 생성
        Exhibition exhibition = new Exhibition();
        exhibition.setLocation(exhibitionRequest.getLocation());
        exhibition.setSchedule(exhibitionRequest.getSchedule());
        exhibition.setTime(exhibitionRequest.getTime());
        exhibition.setUser(user);

        try {
            Exhibition savedExhibition = exhibitionRepository.save(exhibition);
            return exhibitionMapper.toResponse(savedExhibition);
        } catch (Exception e) {
            throw new InternalServerErrorException("전시 정보를 저장하는 동안 오류가 발생했습니다.");
        }
    }
}