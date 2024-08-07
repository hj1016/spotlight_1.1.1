package spotlight.spotlight_ver2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.entity.Exhibition;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.exception.UserNotFoundException;
import spotlight.spotlight_ver2.exception.InternalServerErrorException;
import spotlight.spotlight_ver2.repository.ExhibitionRepository;
import spotlight.spotlight_ver2.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExhibitionServiceTest {

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExhibitionService exhibitionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateExhibition_Success() {
        // Arrange
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setExhibitionId(1L);
        exhibitionDTO.setLocation("서울여자대학교 조형예술관");
        exhibitionDTO.setSchedule("2024-08-07 ~ 2024-08-10");
        exhibitionDTO.setTime("10:00 AM ~ 17:00 PM");
        exhibitionDTO.setUserId(1L);
        exhibitionDTO.setFeedId(1L);

        User user = new User();
        user.setId(1L);

        Feed feed = new Feed();
        feed.setFeedId(1L);

        Exhibition exhibition = new Exhibition();
        exhibition.setExhibitionId(1L);
        exhibition.setLocation("서울여자대학교 조형예술관");
        exhibition.setSchedule("2024-08-07 ~ 2024-08-10");
        exhibition.setTime("10:00 AM ~ 17:00 PM");
        exhibition.setUser(user);
        exhibition.setFeed(feed);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(exhibition);

        // Act
        ExhibitionDTO result = exhibitionService.createExhibition(exhibitionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getExhibitionId());
        assertEquals("서울여자대학교 조형예술관", result.getLocation());
        assertEquals("2024-08-07 ~ 2024-08-10", result.getSchedule());
        assertEquals("10:00 AM ~ 17:00 PM", result.getTime());
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getFeedId());
    }

    @Test
    public void testCreateExhibition_UserNotFound() {
        // Arrange
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            exhibitionService.createExhibition(exhibitionDTO);
        });
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void testCreateExhibition_InternalServerError() {
        // Arrange
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setUserId(1L);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(exhibitionRepository.save(any(Exhibition.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        InternalServerErrorException exception = assertThrows(InternalServerErrorException.class, () -> {
            exhibitionService.createExhibition(exhibitionDTO);
        });
        assertEquals("전시 정보를 저장하는 동안 오류가 발생했습니다.", exception.getMessage());
    }
}