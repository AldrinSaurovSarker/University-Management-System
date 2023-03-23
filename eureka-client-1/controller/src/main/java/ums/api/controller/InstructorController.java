package ums.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ums.api.entities.Instructor;
import ums.api.service.InstructorService;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/instructors")
public class InstructorController {
    public final InstructorService instructorService;
    
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public List<Instructor> getInstructors() {
        return instructorService.getInstructors();
    }

    @GetMapping(path = "{InstructorId}")
    public Instructor getInstructorById(@PathVariable("InstructorId") Long instructorId) {
        return instructorService.getInstructorById(instructorId);
    }

    @PostMapping
    public Instructor createInstructor(@RequestBody Instructor instructor) {
        return instructorService.createInstructor(instructor);
    }

    @DeleteMapping(path = "{InstructorId}")
    public ResponseEntity deleteInstructor(@PathVariable("InstructorId") Long instructorId) {
        instructorService.deleteInstructor(instructorId);
        return ResponseEntity.ok("Instructor deleted with ID: " + instructorId);
    }

    @PutMapping
    public Instructor updateInstructor(@RequestBody Instructor instructor) {
        return instructorService.updateInstructor(instructor);
    }

    @PutMapping(value = "enroll")
    public Instructor assignSections(
            @RequestParam(value = "instructorId") Long instructorId,
            @RequestParam(value = "subjectCode") String subjectCode,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "term") Integer term
    ) {
        return instructorService.assignSections(instructorId, subjectCode, year, term);
    }
}
