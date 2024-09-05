package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "해시태그 DTO")
public class HashtagDTO {

    @Schema(description = "해시태그 ID", example = "1")
    private Integer id;

    @Schema(description = "해시태그 이름", example = "#design")
    private String hashtag;

    @Schema(description = "생성 일자", example = "2024-09-06T12:00:00Z")
    private Timestamp createdDate;

    @Schema(description = "사용자 정보", implementation = UserDTO.class)
    private UserDTO user;

    // 기본 생성자
    public HashtagDTO() {}

    // 매개변수를 받는 생성자
    public HashtagDTO(Integer id, String hashtag, Timestamp createdDate, UserDTO user) {
        this.id = id;
        this.hashtag = hashtag;
        this.createdDate = createdDate;
        this.user = user;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}