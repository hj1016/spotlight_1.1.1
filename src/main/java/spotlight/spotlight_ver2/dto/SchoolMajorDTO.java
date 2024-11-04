package spotlight.spotlight_ver2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "학교/학과 이름 DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolMajorDTO {

    @Schema(description = "학교 이름", example = "서울여자대학교")
    private String schoolName;

    @Schema(description = "학과 이름", example = "소프트웨어융합학과")
    private String majorName;

    // 기본 생성자 추가
    public SchoolMajorDTO() {}

    public SchoolMajorDTO(String schoolName, String majorName) {
        this.schoolName = schoolName;
        this.majorName = majorName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }
}