package ums.api.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ums.api.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentSpec {
    public static Specification<Department> getDepartments(
            String deptCode, String deptName, String deptAbbrev, String building
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if (deptCode != null && !(deptCode.isEmpty())) {
                predicate.add(criteriaBuilder.equal(root.get("deptCode"), deptCode));
            }

            if (deptName != null && !(deptName.isEmpty())) {
                predicate.add(criteriaBuilder.like(root.get("deptName"), "%"+deptName+"%"));
            }

            if (deptAbbrev != null && !(deptAbbrev.isEmpty())) {
                predicate.add(criteriaBuilder.equal(root.get("deptAbbrev"), deptAbbrev));
            }

            if (building != null && !(building.isEmpty())) {
                predicate.add(criteriaBuilder.like(root.get("building"), "%"+building+"%"));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        });
    }
}
