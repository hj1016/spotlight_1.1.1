package spotlight.spotlight_ver2.response;

import spotlight.spotlight_ver2.dto.SchoolMajorDTO;

import java.util.List;

public class MajorApiResponse {
    private List<SchoolMajorDTO> majorList;

    // Getters and setters
    public List<SchoolMajorDTO> getMajorList() {
        return majorList;
    }

    public void setMajorList(List<SchoolMajorDTO> majorList) {
        this.majorList = majorList;
    }
}
