package spotlight.spotlight_ver2.dto;

public class StageDTO {
    private Long id;

    // 기본 생성자
    public StageDTO() {}

    // 매개변수를 받는 생성자
    public StageDTO(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}