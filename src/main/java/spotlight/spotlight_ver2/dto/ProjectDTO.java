package spotlight.spotlight_ver2.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProjectDTO {
    private Long id;
    private String name;
    private Set<FeedDTO> feeds = new HashSet<>();
    private Map<StudentDTO, ProjectRoleDTO> studentRoles;

    // 기본 생성자
    public ProjectDTO() {}

    // 매개변수를 받는 생성자
    public ProjectDTO(Long id, String name, Set<FeedDTO> feeds, Map<StudentDTO, ProjectRoleDTO> studentRoles) {
        this.id = id;
        this.name = name;
        this.feeds = feeds;
        this.studentRoles = studentRoles;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FeedDTO> getFeeds() {
        return feeds;
    }

    public void setFeeds(Set<FeedDTO> feeds) {
        this.feeds = feeds;
    }

    public Map<StudentDTO, ProjectRoleDTO> getStudentRoles() {
        return studentRoles;
    }

    public void setStudentRoles(Map<StudentDTO, ProjectRoleDTO> studentRoles) {
        this.studentRoles = studentRoles;
    }
}