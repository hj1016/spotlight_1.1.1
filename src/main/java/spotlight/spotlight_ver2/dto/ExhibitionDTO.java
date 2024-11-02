package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Exhibition;

@Getter
@Setter
@Schema(description = "전시 정보 DTO")
public class ExhibitionDTO {

    @Schema(description = "전시 ID", example = "1")
    private Long exhibitionId;

    @Schema(description = "전시 위치", example = "서울여자대학교 조형예술관")
    private String location;

    @Schema(description = "전시 일정", example = "2024-08-15")
    private String schedule;

    @Schema(description = "전시 시간", example = "10:00 AM")
    private String time;

    @Schema(description = "사용자 요약 정보", implementation = ExhibitionUserDTO.class)
    private ExhibitionUserDTO user;

    @Schema(description = "피드 요약 정보", implementation = ExhibitionFeedDTO.class)
    private ExhibitionFeedDTO feed;

    public ExhibitionDTO() {}

    public ExhibitionDTO(Exhibition exhibition) {
        this.exhibitionId = exhibition.getExhibitionId();
        this.location = exhibition.getLocation();
        this.schedule = exhibition.getSchedule();
        this.time = exhibition.getTime();
        this.user = (exhibition.getUser() != null) ? new ExhibitionUserDTO(exhibition.getUser().getId()) : null;
        this.feed = (exhibition.getFeed() != null) ? new ExhibitionFeedDTO(exhibition.getFeed().getFeedId(), exhibition.getFeed().getTitle()) : null;
    }

    @Getter
    @Setter
    public static class ExhibitionUserDTO {
        @Schema(description = "사용자 ID", example = "123")
        private Long id;

        public ExhibitionUserDTO() {}

        public ExhibitionUserDTO(Long id) {
            this.id = id;
        }
    }

    @Getter
    public static class ExhibitionFeedDTO {
        @Schema(description = "피드 ID", example = "456")
        private Long feedId;

        @Schema(description = "피드 제목", example = "Feed Title")
        private String title;

        public ExhibitionFeedDTO() {}

        public ExhibitionFeedDTO(Long feedId, String title) {
            this.feedId = feedId;
            this.title = title;
        }
    }

    // user와 feed의 ID를 반환하는 메서드
    public Long getUserId() {
        return this.user != null ? this.user.getId() : null;
    }

    public Long getFeedId() {
        return this.feed != null ? this.feed.getFeedId() : null;
    }
}