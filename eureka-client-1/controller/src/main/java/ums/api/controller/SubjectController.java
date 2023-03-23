package ums.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ums.api.entities.Subject;
import ums.api.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }

    @GetMapping(path = "{subjectCode}")
    public Subject getSubjectBySubjectCode(@PathVariable String subjectCode) {
        return subjectService.getSubjectBySubjectCode(subjectCode);
    }

    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectService.createSubject(subject);
    }

    @DeleteMapping(path = "{subjectCode}")
    public ResponseEntity<String> deleteSubject(@PathVariable String subjectCode) {
        subjectService.deleteSubject(subjectCode);
        return ResponseEntity.ok("Subject deleted with Code: " + subjectCode);
    }

    @PutMapping
    public Subject updateStudent(@RequestBody Subject subject) {
        return subjectService.updateSubject(subject);
    }
}
