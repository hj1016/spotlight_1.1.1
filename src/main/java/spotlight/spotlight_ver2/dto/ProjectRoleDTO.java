package spotlight.spotlight_ver2.dto;

public class ProjectRoleDTO {
    private Long id;
    private StudentDTO student;
    private ProjectDTO project;
    private String role;

    // 기본 생성자
    public ProjectRoleDTO() {}

    // 매개변수를 받는 생성자
    public ProjectRoleDTO(Long id, StudentDTO student, ProjectDTO project, String role) {
        this.id = id;
        this.student = student;
        this.project = project;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}