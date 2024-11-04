package spotlight.spotlight_ver2.response;

import spotlight.spotlight_ver2.dto.SchoolMajorDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MajorApiResponse {

    @JsonProperty("dataSearch")
    private DataSearch dataSearch;

    // Getters and setters
    public DataSearch getDataSearch() {
        return dataSearch;
    }

    public void setDataSearch(DataSearch dataSearch) {
        this.dataSearch = dataSearch;
    }

    public List<SchoolMajorDTO> getMajorList() {
        return dataSearch != null ? dataSearch.getContent() : null;
    }

    // 내부 클래스 정의: DataSearch
    public static class DataSearch {

        @JsonProperty("content")
        private List<SchoolMajorDTO> content;

        // Getters and setters
        public List<SchoolMajorDTO> getContent() {
            return content;
        }

        public void setContent(List<SchoolMajorDTO> content) {
            this.content = content;
        }
    }
}