package ums.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ums.api.entities.Department;
import ums.api.entities.Section;
import ums.api.entities.SectionSerializable;
import ums.api.entities.Student;
import ums.api.repositories.DepartmentRepository;
import ums.api.repositories.SectionRepository;
import ums.api.repositories.StudentRepository;
import ums.api.repositories.SubjectRepository;
import ums.api.utils.AlreadyExistsException;
import ums.api.utils.InvalidStateException;
import ums.api.utils.NotFoundException;

import java.time.LocalDate;
import java.util.*;

@Service
public class StudentService {
    public final StudentRepository studentRepository;
    public final SubjectRepository subjectRepository;
    public final DepartmentRepository departmentRepository;
    public final SectionRepository sectionRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, SubjectRepository subjectRepository, DepartmentRepository departmentRepository, SectionRepository sectionRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentsByEmail(student.getEmail());

        if (studentRepository.findStudentsById(student.getId()).isPresent()) {
            throw new AlreadyExistsException("A student already exists with this student id");
        }

        if (studentOptional.isPresent()) {
            throw new AlreadyExistsException("A student already exists with this email account");
        } else {
            String numStr = Long.toString(student.getId());
            String departmentCode = numStr.substring(2, 4);

            Optional<Department> departmentOptional = departmentRepository.findById(departmentCode);

            if (departmentOptional.isPresent()) {
                student.setDepartment(departmentOptional.get());
                return studentRepository.save(student);
            } else {
                throw new InvalidStateException("Invalid student Id. No department exists with department code " + departmentCode);
            }
        }
    }

    public Student getStudentById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findStudentsById(studentId);

        if (studentOptional.isEmpty()) {
            throw new NotFoundException("Student with id " + studentId + " doesn't exists");
        } else {
            return studentOptional.get();
        }
    }

    public void deleteStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findStudentsById(studentId);

        if (studentOptional.isPresent()) {
            studentRepository.deleteById(studentId);
        } else {
            throw new NotFoundException("Student with id " + studentId + " doesn't exists");
        }
    }

    public Student updateStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findById(student.getId());

        if (studentOptional.isPresent()) {
            Student std = studentOptional.get();
            String name = student.getName();
            String email = student.getEmail();
            LocalDate dob = student.getDob();

            if (name != null && name.length() > 0) {
                std.setName(name);
            }

            if (email != null && email.length() > 0 && !Objects.equals(email, std.getEmail())) {
                Optional<Student> emailExists = studentRepository.findStudentsByEmail(email);

                if (emailExists.isPresent()) {
                    throw new AlreadyExistsException("A student already exists with this email account");
                } else {
                    std.setEmail(email);
                }
            }

            if (dob != null) {
                std.setDob(dob);
            }
            return studentRepository.save(std);
        } else {
            throw new NotFoundException("Student with id " + student.getId() + " doesn't exists");
        }
    }

    public Student enrollSections(Long studentId, String subjectCode, Integer year, Integer term) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isEmpty()) {
            throw new NotFoundException("Student with id " + studentId + " doesn't exists");
        } else {
            Student stud = studentOptional.get();
            SectionSerializable sectionSerializable = new SectionSerializable(subjectCode, year, term);
            Optional<Section> sectionOptional = sectionRepository.findById(sectionSerializable);

            if (sectionOptional.isEmpty()) {
                throw new NotFoundException("The following Id doesn't belong to any section : " + sectionSerializable);
            }
            stud.setCredits(stud.getCredits() + sectionOptional.get().getSubject().getCredit());
            Set<Section> enrolledSectionSet = stud.getSections();
            enrolledSectionSet.add(sectionOptional.get());
            stud.setSections(enrolledSectionSet);

            return studentRepository.save(stud);
        }
    }
}
