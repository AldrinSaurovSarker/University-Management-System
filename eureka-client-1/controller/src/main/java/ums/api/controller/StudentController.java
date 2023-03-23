package ums.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ums.api.entities.Student;
import ums.api.service.StudentService;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping(path = "{studentId}")
    public Student getStudentById(@PathVariable("studentId") Long studentId) {
        return studentService.getStudentById(studentId);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public ResponseEntity deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student deleted with ID: " + studentId);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @PutMapping(value = "enroll")
    public Student enrollSections(
            @RequestParam(value = "studentId") Long studentId,
            @RequestParam(value = "subjectCode") String subjectCode,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "term") Integer term
    ) {
        return studentService.enrollSections(studentId, subjectCode, year, term);
    }
}
