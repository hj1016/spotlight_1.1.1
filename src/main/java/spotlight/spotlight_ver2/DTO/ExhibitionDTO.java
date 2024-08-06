package spotlight.spotlight_ver2.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExhibitionDTO {
    @Schema(description = "전시 ID", example = "1")
    private Long exhibitionId;

    @Schema(description = "전시 위치", example = "서울여자대학교 조형예술관")
    private String location;

    @Schema(description = "전시 일정", example = "2024-08-15")
    private String schedule;

    @Schema(description = "전시 시간", example = "10:00 AM")
    private String time;

    @Schema(description = "사용자 ID", example = "123")
    private Long userId;

    @Schema(description = "피드 ID", example = "456")
    private Long feedId;

    // Default constructor
    public ExhibitionDTO() {
    }

    // Parameterized constructor
    public ExhibitionDTO(Long exhibitionId, String location, String schedule, String time, Long userId, Long feedId) {
        this.exhibitionId = exhibitionId;
        this.location = location;
        this.schedule = schedule;
        this.time = time;
        this.userId = userId;
        this.feedId = feedId;
    }

    // Getters and Setters
    public Long getExhibitionId() {
        return exhibitionId;
    }

    public void setExhibitionId(Long exhibitionId) {
        this.exhibitionId = exhibitionId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }
}