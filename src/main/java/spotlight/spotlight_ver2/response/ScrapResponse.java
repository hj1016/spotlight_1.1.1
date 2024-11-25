package spotlight.spotlight_ver2.response;

public class ScrapResponse {
    private boolean success;        // 요청 성공 여부
    private String message;         // 응답 메시지
    private Long targetId;          // 대상 ID (Feed ID 또는 Student ID)
    private String targetType;      // 대상 유형 ("Feed" 또는 "Student")
    private int scrapCount;         // 현재 스크랩 수

    public ScrapResponse(boolean success, String message, Long targetId, String targetType, int scrapCount) {
        this.success = success;
        this.message = message;
        this.targetId = targetId;
        this.targetType = targetType;
        this.scrapCount = scrapCount;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public int getScrapCount() {
        return scrapCount;
    }

    public void setScrapCount(int scrapCount) {
        this.scrapCount = scrapCount;
    }
}