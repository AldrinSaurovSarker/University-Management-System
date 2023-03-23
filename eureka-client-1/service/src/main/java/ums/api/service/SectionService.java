package ums.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ums.api.entities.Section;
import ums.api.entities.SectionSerializable;
import ums.api.repositories.SectionRepository;
import ums.api.repositories.SubjectRepository;
import ums.api.spec.SectionSpec;
import ums.api.utils.AlreadyExistsException;
import ums.api.utils.InvalidStateException;
import ums.api.utils.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    public final SectionRepository sectionRepository;
    public final SubjectRepository subjectRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository, SubjectRepository subjectRepository) {
        this.sectionRepository = sectionRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Section> getSections(String subjectCode, Integer year, Integer term) {
        Specification<Section> sectionSpecification = SectionSpec.getSections(subjectCode, year, term);
        return sectionRepository.findAll(sectionSpecification);
    }

    public Section createSection(Section section) {
        String subjectCode = section.getSubjectCode();
        if (!subjectRepository.existsById(subjectCode)) {
            throw new NotFoundException("Subject with subject code " + subjectCode + " doesn't exists.");
        }
        SectionSerializable sectionSerializable = new SectionSerializable(
                subjectCode,
                section.getYear(),
                section.getTerm());
        if (sectionRepository.existsById(sectionSerializable)) {
            throw new AlreadyExistsException("Section with the provided {subject code, year, term} already exists.");
        }

        if (section.getEndTime().isBefore(section.getStartTime())) {
            throw new InvalidStateException("End time cannot be before start time");
        }

        return sectionRepository.save(section);
    }

    public ResponseEntity<String> deleteSection(String subjectCode, Integer year, Integer term) {
        SectionSerializable sectionId = new SectionSerializable(subjectCode, year, term);
        Optional<Section> section = sectionRepository.findById(sectionId);

        if (section.isPresent()) {
            sectionRepository.deleteById(sectionId);
            return ResponseEntity.ok("Section deleted with ID: " + sectionId);
        } else {
            throw new NotFoundException("Section not found with ID: " + sectionId);
        }
    }

    public Section updateSection(Section section) {
        SectionSerializable sectionId = new SectionSerializable(
                section.getSubjectCode(),
                section.getYear(),
                section.getTerm());
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        if (sectionOptional.isPresent()) {
            Section sec = sectionOptional.get();
            LocalDate startTime = section.getStartTime();
            LocalDate endTime = section.getEndTime();

            if (startTime != null) {
                sec.setStartTime(startTime);
            }

            if (endTime != null) {
                sec.setEndTime(endTime);
            }

            if (sec.getEndTime().isBefore(sec.getStartTime())) {
                throw new InvalidStateException("End time cannot be before start time");
            }

            return sectionRepository.save(sec);
        } else {
            throw new NotFoundException("Section not found with ID: " + sectionId);
        }
    }
}
