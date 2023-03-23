package ums.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ums.api.entities.Department;
import ums.api.repositories.DepartmentRepository;
import ums.api.spec.DepartmentSpec;
import ums.api.utils.AlreadyExistsException;
import ums.api.utils.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getDepartments(String deptCode, String deptName, String deptAbbrev, String building) {
        Specification<Department> departmentSpecification = DepartmentSpec.getDepartments(deptCode, deptName, deptAbbrev, building);
        return departmentRepository.findAll(departmentSpecification);
    }

    public Department createDepartment(Department department) {
        List<Department> departmentOptional = departmentRepository.findByDeptNameOrDeptCodeOrDeptAbbrev(
                department.getDeptName(), department.getDeptCode(), department.getDeptAbbrev());

        if (departmentOptional.isEmpty()) {
            return departmentRepository.save(department);
        } else {
            throw new AlreadyExistsException("Department with same department code, name or abbreviation already exists");
        }
    }

    public void deleteDepartment(String deptCode) {
        Optional<Department> departmentOptional = departmentRepository.findById(deptCode);

        if (departmentOptional.isEmpty()) {
            throw new NotFoundException("Department with department code " + deptCode + " doesn't exists");
        } else {
            departmentRepository.deleteById(deptCode);
        }
    }

    public Department updateDepartment(Department department) {
        String deptCode = department.getDeptCode();
        Optional<Department> departmentOptional = departmentRepository.findById(deptCode);

        if (departmentOptional.isPresent()) {
            Department dept = departmentOptional.get();
            String deptName = department.getDeptName();
            String deptAbbrev = department.getDeptAbbrev();
            String building = department.getBuilding();

            if (deptName != null && deptName.length() > 0 && !Objects.equals(deptName, dept.getDeptName())) {
                Optional<Department> department1 = departmentRepository.findDepartmentByDeptName(deptName);

                if (department1.isPresent()) {
                    throw new AlreadyExistsException("Department with same department name already exists");
                } else {
                    dept.setDeptName(deptName);
                }
            }

            if (deptAbbrev != null && deptAbbrev.length() > 0 && !Objects.equals(deptAbbrev, dept.getDeptAbbrev())) {
                Optional<Department> department2 = departmentRepository.findDepartmentByDeptName(deptAbbrev);

                if (department2.isPresent()) {
                    throw new AlreadyExistsException("Department with same department abbreviation already exists");
                } else {
                    dept.setDeptAbbrev(deptAbbrev);
                }
            }

            if (building != null && building.length() > 0) {
                dept.setBuilding(building);
            }

            return departmentRepository.save(dept);
        } else {
            throw new NotFoundException("Department with department code " + deptCode + " doesn't exists");
        }
    }
}
