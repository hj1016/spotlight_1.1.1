package spotlight.spotlight_ver2.response;

import spotlight.spotlight_ver2.dto.SchoolMajorDTO;

import java.util.List;

public class SchoolApiResponse {
    private List<SchoolMajorDTO> schoolList;

    // Getters and setters
    public List<SchoolMajorDTO> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<SchoolMajorDTO> schoolList) {
        this.schoolList = schoolList;
    }
}
