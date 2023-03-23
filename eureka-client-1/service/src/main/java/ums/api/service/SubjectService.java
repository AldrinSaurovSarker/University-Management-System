package ums.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ums.api.entities.Department;
import ums.api.entities.Subject;
import ums.api.repositories.DepartmentRepository;
import ums.api.repositories.StudentRepository;
import ums.api.repositories.SubjectRepository;
import ums.api.utils.AlreadyExistsException;
import ums.api.utils.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    public final DepartmentRepository departmentRepository;
    public final StudentRepository studentRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, DepartmentRepository departmentRepository, StudentRepository studentRepository) {
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
    }

    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectBySubjectCode(String subjectCode) {
        Optional<Subject> subjectOptional = subjectRepository.findSubjectBySubjectCode(subjectCode);

        if (subjectOptional.isEmpty()) {
            throw new NotFoundException("Subject with subject code " + subjectCode + " doesn't exists");
        } else {
            return subjectOptional.get();
        }
    }

    public Subject createSubject(Subject subject) {
        Optional<Subject> subjectOptional = subjectRepository.findSubjectBySubjectCode(subject.getSubjectCode());

        if (subjectOptional.isPresent()) {
            throw new AlreadyExistsException("Subject with subject code " + subject.getSubjectCode() + " already exists");
        }

        String subCode = subject.getSubjectCode();
        String deptAbbrev = subCode.substring(0, subCode.indexOf("-"));
        Optional<Department> department = departmentRepository.findDepartmentByDeptAbbrev(deptAbbrev);
        department.ifPresent(subject::setDepartment);

        return subjectRepository.save(subject);
    }

    public void deleteSubject(String subjectCode) {
        Optional<Subject> subjectOptional = subjectRepository.findSubjectBySubjectCode(subjectCode);

        if (subjectOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            subjectRepository.delete(subject);
        } else {
            throw new NotFoundException("No subject exists with subject code " + subjectCode);
        }
    }

    public Subject updateSubject(Subject subject) {
        Optional<Subject> subjectOptional = subjectRepository.findSubjectBySubjectCode(subject.getSubjectCode());
        String subjectCode = subject.getSubjectCode();

        if (subjectOptional.isPresent()) {
            Subject sub = subjectOptional.get();
            String subjectName = subject.getSubjectName();
            Double credit = subject.getCredit();

            if (subjectName != null && subjectName.length() > 0) {
                sub.setSubjectName(subjectName);
            }

            if (credit != null) {
                sub.setCredit(credit);
            }

            return subjectRepository.save(sub);
        } else {
            if (subjectCode == null) {
                throw new NotFoundException("You should provide a subject code");
            } else if (subjectCode.length() == 0) {
                throw new NotFoundException("Subject code can't be empty");
            } else {
                throw new NotFoundException("No subject exists with subject code " + subjectCode);
            }
        }
    }
}
