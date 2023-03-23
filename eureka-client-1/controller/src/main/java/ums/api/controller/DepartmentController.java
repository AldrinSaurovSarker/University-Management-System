package ums.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ums.api.entities.Department;
import ums.api.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<Department> getDepartments(
            @RequestParam(value = "deptCode", required = false) String deptCode,
            @RequestParam(value = "deptName", required = false) String deptName,
            @RequestParam(value = "deptAbbrev", required = false) String deptAbbrev,
            @RequestParam(value = "building", required = false) String building
    ) {
        return departmentService.getDepartments(deptCode, deptName, deptAbbrev, building);
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @DeleteMapping(path = "{deptCode}")
    public ResponseEntity deleteDepartment(@PathVariable("deptCode") String deptCode) {
        departmentService.deleteDepartment(deptCode);
        return ResponseEntity.ok("Department deleted with code: " + deptCode);
    }

    @PutMapping
    public Department updateDepartment(@RequestBody Department department) {
        return departmentService.updateDepartment(department);
    }
}
