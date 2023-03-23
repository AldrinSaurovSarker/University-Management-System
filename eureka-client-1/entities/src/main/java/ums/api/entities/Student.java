package ums.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Student {
    @Id
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "age")
    private Integer age;

    @Column(name = "credits")
    private Double credits;

    @ManyToOne
    @JoinColumn(name = "dept_name", referencedColumnName = "dept_name")
    private Department department;

    @ManyToMany
    @JoinTable(name = "enrolls",
        joinColumns = {
                @JoinColumn(name = "student_id", referencedColumnName = "student_id")
        },
        inverseJoinColumns = {
                @JoinColumn(name = "subject_code", referencedColumnName = "subject_code"),
                @JoinColumn(name = "year", referencedColumnName = "year"),
                @JoinColumn(name = "term", referencedColumnName = "term")
        }
    )
    private Set<Section> sections = new HashSet<>();

    public Student(Long id, String email) {
        this.id = id;
        this.email = email;
        this.credits = 0.0;
    }

    public Student(Long id, Set<Section> sections) {
        this.id = id;
        this.sections = sections;
        this.credits = 0.0;
    }

    public Student(Long id, String email, Set<Section> sections) {
        this.id = id;
        this.email = email;
        this.sections = sections;
        this.credits = 0.0;
    }

    public Student(Long id, String name, String email, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.age = this.getAge();
        this.credits = 0.0;
    }

    public Student(Long id, String name, String email, LocalDate dob, Set<Section> sections) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.sections = sections;
        this.age = this.getAge();
        this.credits = 0.0;
    }

    public Integer getAge() {
        try {
            return Period.between(this.dob, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }

    @PrePersist
    private void prePersistAge() {
        this.age = getAge();
    }
}
