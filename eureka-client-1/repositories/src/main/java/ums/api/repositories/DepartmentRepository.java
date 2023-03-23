package ums.api.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ums.api.entities.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository  extends JpaRepository<Department, String> {
    Optional<Department> findDepartmentByDeptAbbrev(String deptAbbrev);
    Optional<Department> findDepartmentByDeptName(String deptName);
    List<Department> findDepartmentByBuilding(String building);
    List<Department> findByDeptNameOrDeptCodeOrDeptAbbrev(String deptName, String deptCode, String deptAbbrev);

    List<Department> findAll(Specification<Department> departmentSpecification);
}
