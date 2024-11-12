package spotlight.spotlight_ver2.dto;

public class StudentRecommendationDTO {
    private String studentName;
    private String feedCategory;

    // 생성자
    public StudentRecommendationDTO(String studentName, String feedCategory) {
        this.studentName = studentName;
        this.feedCategory = feedCategory;
    }

    // Getter와 Setter
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
