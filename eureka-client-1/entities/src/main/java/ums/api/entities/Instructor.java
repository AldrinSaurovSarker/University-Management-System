package ums.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    @Id
    @Column(name = "instructor_id")
    private Long id;

    @Column(name = "instructor_name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "designation")
    private String designation;

    @Column(name = "salary")
    private Long salary;

    @ManyToOne
    @JoinColumn(name = "dept_name", referencedColumnName = "dept_name")
    private Department department;

    @ManyToMany(mappedBy = "instructors")
    @JsonIgnore
    private Set<Section> sections = new HashSet<>();

    public Instructor(Long id, String name, String email, String designation, Long salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.salary = salary;
    }
}
