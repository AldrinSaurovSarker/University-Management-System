package ums.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ums.api.entities.Section;
import ums.api.service.SectionService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/sections")
public class SectionController {
    public final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<Section> getSections(
            @RequestParam(value = "subjectCode", required = false) String subjectCode,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "term", required = false) Integer term
    ) {
        return sectionService.getSections(subjectCode, year, term);
    }

    @PostMapping
    public Section createSection(@RequestBody Section section) {
        return sectionService.createSection(section);
    }

    @DeleteMapping
    public ResponseEntity deleteSection(
            @RequestParam(value = "subjectCode") String subjectCode,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "term") Integer term) {
        if (subjectCode == null || year == null || term == null) {
            return ResponseEntity.badRequest().body("Invalid input: must provide subjectCode, year, and term");
        }

        return sectionService.deleteSection(subjectCode, year, term);
    }

    @PutMapping
    public Section updateSection(@RequestBody Section section) {
        return sectionService.updateSection(section);
    }
}
