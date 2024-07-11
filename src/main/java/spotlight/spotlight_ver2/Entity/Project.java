package spotlight.spotlight_ver2.Entity;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feed> feeds = new HashSet<>();


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "student")
    @Column(name = "role")
    private Map<Student, ProjectRole> studentRoles = new HashMap<>();

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

    public Set<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(Set<Feed> feeds) {
        this.feeds = feeds;
    }

    public Map<Student, ProjectRole> getStudentRoles() {
        return studentRoles;
    }

    public void setStudentRoles(Map<Student, ProjectRole> studentRoles) {
        this.studentRoles = studentRoles;
    }
}
