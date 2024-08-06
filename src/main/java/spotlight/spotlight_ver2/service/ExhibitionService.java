package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.DTO.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.ExhibitionRepository;
import spotlight.spotlight_ver2.repository.UserRepository;

@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final UserRepository userRepository;

    public ExhibitionService(ExhibitionRepository exhibitionRepository, UserRepository userRepository) {
        this.exhibitionRepository = exhibitionRepository;
        this.userRepository = userRepository;
    }

    public ExhibitionDTO createExhibition(ExhibitionDTO exhibitionDTO) {
        User user = userRepository.findById(exhibitionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Exhibition exhibition = new Exhibition();
        exhibition.setLocation(exhibitionDTO.getLocation());
        exhibition.setSchedule(exhibitionDTO.getSchedule());
        exhibition.setTime(exhibitionDTO.getTime());
        exhibition.setUser(user);
        Exhibition savedExhibition = exhibitionRepository.save(exhibition);
        return convertToDTO(savedExhibition);
    }

    private ExhibitionDTO convertToDTO(Exhibition exhibition) {
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setExhibitionId(exhibition.getExhibitionId());
        exhibitionDTO.setLocation(exhibition.getLocation());
        exhibitionDTO.setSchedule(exhibition.getSchedule());
        exhibitionDTO.setTime(exhibition.getTime());
        exhibitionDTO.setUserId(exhibition.getUser() != null ? exhibition.getUser().getId() : null);
        exhibitionDTO.setFeedId(exhibition.getFeed() != null ? exhibition.getFeed().getFeedId() : null);
        return exhibitionDTO;
    }
}