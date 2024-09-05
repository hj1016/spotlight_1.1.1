package spotlight.spotlight_ver2.dto;

public class SchoolMajorDTO {
    private String schoolName;
    private String majorName;

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