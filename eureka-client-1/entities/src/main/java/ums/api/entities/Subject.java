package ums.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @Column(name = "subject_code")
    private String subjectCode;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "credit")
    private Double credit;

    @ManyToOne
    @JoinColumn(name = "dept_name", referencedColumnName = "dept_name")
    private Department department;
}
