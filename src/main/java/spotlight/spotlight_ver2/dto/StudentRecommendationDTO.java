package spotlight.spotlight_ver2.dto;

public class StudentRecommendationDTO {

    private String username;
    private String studentName;
    private String feedCategory;

    // 생성자
    public StudentRecommendationDTO(String username, String studentName, String feedCategory) {
        this.username = username;
        this.studentName = studentName;
        this.feedCategory = feedCategory;
    }

    // Getter와 Setter
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(String feedCategory) {
        this.feedCategory = feedCategory;
    }
}
