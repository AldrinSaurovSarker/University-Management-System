package ums.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(SectionSerializable.class)
public class Section {
    @Id
    @Column(name = "subject_code")
    private String subjectCode;

    @Id
    @Column(name = "year")
    private Integer year;

    @Id
    @Column(name = "term")
    private Integer term;

    @ManyToMany
    @JoinTable(name = "teaches",
            joinColumns = {
                    @JoinColumn(name = "subject_code", referencedColumnName = "subject_code"),
                    @JoinColumn(name = "year", referencedColumnName = "year"),
                    @JoinColumn(name = "term", referencedColumnName = "term")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "instructor_id", referencedColumnName = "instructor_id")
            }
    )
    private List<Instructor> instructors;

    @ManyToOne
    @JoinColumn(name = "subject_code", referencedColumnName = "subject_code", insertable = false, updatable = false)
    private Subject subject;

    @ManyToMany(mappedBy = "sections")
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    @Temporal(TemporalType.DATE)
    @Nullable
    private LocalDate startTime;

    @Temporal(TemporalType.DATE)
    @Nullable
    private LocalDate endTime;
}
