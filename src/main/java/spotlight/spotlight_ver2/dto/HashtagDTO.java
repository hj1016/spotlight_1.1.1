package spotlight.spotlight_ver2.dto;

import java.sql.Timestamp;

public class HashtagDTO {
    private Integer id;
    private String hashtag;
    private Timestamp createdDate;
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