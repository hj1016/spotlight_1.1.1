package spotlight.spotlight_ver2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spotlight.spotlight_ver2.dto.ExhibitionDTO;
import spotlight.spotlight_ver2.exception.UserNotFoundException;
import spotlight.spotlight_ver2.exception.InternalServerErrorException;
import spotlight.spotlight_ver2.service.ExhibitionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ExhibitionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExhibitionService exhibitionService;

    @InjectMocks
    private ExhibitionController exhibitionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(exhibitionController).build();
    }

    @Test
    public void testCreateExhibition_Success() throws Exception {
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setExhibitionId(1L);
        exhibitionDTO.setLocation("서울여자대학교 조형예술관");
        exhibitionDTO.setSchedule("2024-08-07 ~ 2024-08-10");
        exhibitionDTO.setTime("10:00 AM ~ 17:00 PM");
        exhibitionDTO.setUserId(1L);
        exhibitionDTO.setFeedId(1L);

        when(exhibitionService.createExhibition(any(ExhibitionDTO.class))).thenReturn(exhibitionDTO);

        mockMvc.perform(post("/api/exhibitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exhibitionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.exhibition.exhibitionId").value(1))
                .andExpect(jsonPath("$.exhibition.location").value("서울여자대학교 조형예술관"))
                .andExpect(jsonPath("$.exhibition.schedule").value("2024-08-07 ~ 2024-08-10"))
                .andExpect(jsonPath("$.exhibition.time").value("10:00 AM ~ 17:00 PM"))
                .andExpect(jsonPath("$.exhibition.userId").value(1))
                .andExpect(jsonPath("$.exhibition.feedId").value(1));
    }

    @Test
    public void testCreateExhibition_UserNotFound() throws Exception {
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();
        exhibitionDTO.setUserId(1L);

        when(exhibitionService.createExhibition(any(ExhibitionDTO.class))).thenThrow(new UserNotFoundException("사용자를 찾을 수 없습니다."));

        mockMvc.perform(post("/api/exhibitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exhibitionDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

    @Test
    public void testCreateExhibition_InternalServerError() throws Exception {
        ExhibitionDTO exhibitionDTO = new ExhibitionDTO();

        when(exhibitionService.createExhibition(any(ExhibitionDTO.class)))
                .thenThrow(new InternalServerErrorException("전시 정보를 저장하는 동안 오류가 발생했습니다."));

        mockMvc.perform(post("/api/exhibitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exhibitionDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("전시 정보를 저장하는 동안 오류가 발생했습니다."));
    }
}