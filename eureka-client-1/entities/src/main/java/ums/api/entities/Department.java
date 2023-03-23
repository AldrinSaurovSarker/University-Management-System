package ums.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departments", uniqueConstraints = @UniqueConstraint(columnNames = {"dept_name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department {
    @Id
    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "dept_abbrev")
    private String deptAbbrev;

    @Column(name = "buidling")
    private String building;

    public Department(String deptName) {
        this.deptCode = String.valueOf((int)(Math.random() * 1000));
        this.deptName = deptName;
    }
}
