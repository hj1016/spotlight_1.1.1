package spotlight.spotlight_ver2.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MajorDetailApiResponse {

    @JsonProperty("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {

        @JsonProperty("content")
        private List<Content> content;

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }
    }

    public static class Content {

        @JsonProperty("schoolName")
        private String schoolName;

        @JsonProperty("majorName")
        private String majorName;

        @JsonProperty("majorSeq")
        private String majorSeq;

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

        public String getMajorSeq() {
            return majorSeq;
        }

        public void setMajorSeq(String majorSeq) {
            this.majorSeq = majorSeq;
        }
    }
}