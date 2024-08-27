package spotlight.spotlight_ver2.service;

import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.InternalServerErrorException;
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
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Exhibition exhibition = new Exhibition();
        exhibition.setLocation(exhibitionDTO.getLocation());
        exhibition.setSchedule(exhibitionDTO.getSchedule());
        exhibition.setTime(exhibitionDTO.getTime());
        exhibition.setUser(user);

        // 필요한 경우 여기서 피드를 선택적으로 처리
        // if (exhibitionDTO.getFeedId() != null) {
        //     Feed feed = new Feed();
        //     feed.setFeedId(exhibitionDTO.getFeedId());
        //     exhibition.setFeed(feed);
        // }

        try {
            Exhibition savedExhibition = exhibitionRepository.save(exhibition);
            return convertToDTO(savedExhibition);
        } catch (Exception e) {
            throw new InternalServerErrorException("전시 정보를 저장하는 동안 오류가 발생했습니다.");
        }
    }

    private ExhibitionDTO convertToDTO(Exhibition exhibition) {
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setExhibitionId(exhibition.getExhibitionId());
        exhibitionDTO.setLocation(exhibition.getLocation());
        exhibitionDTO.setSchedule(exhibition.getSchedule());
        exhibitionDTO.setTime(exhibition.getTime());
        exhibitionDTO.setUserId(exhibition.getUser() != null ? exhibition.getUser().getId() : null);

        // 해당하는 경우 피드가 제대로 참조되었는지 확인
        exhibitionDTO.setFeedId(exhibition.getFeed() != null ? exhibition.getFeed().getFeedId() : null);

        return exhibitionDTO;
    }
}