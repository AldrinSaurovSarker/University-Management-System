package ums.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ums.api.entities.*;
import ums.api.repositories.DepartmentRepository;
import ums.api.repositories.InstructorRepository;
import ums.api.repositories.SectionRepository;
import ums.api.utils.AlreadyExistsException;
import ums.api.utils.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class InstructorService {
    public final InstructorRepository instructorRepository;
    public final DepartmentRepository departmentRepository;
    public final SectionRepository sectionRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, DepartmentRepository departmentRepository, SectionRepository sectionRepository) {
        this.instructorRepository = instructorRepository;
        this.departmentRepository = departmentRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }

    public Instructor createInstructor(Instructor instructor) {
        Optional<Instructor> instructorOptional = instructorRepository.findInstructorsByEmail(instructor.getEmail());

        if (instructorRepository.findById(instructor.getId()).isPresent()) {
            throw new AlreadyExistsException("An instructor already exists with this instructor id");
        }

        if (instructorOptional.isPresent()) {
            throw new AlreadyExistsException("An instructor already exists with this email account");
        } else {
            Department department = instructor.getDepartment();

            if (department == null) {
                return instructorRepository.save(instructor);
            }

            Optional<Department> departmentOptional = departmentRepository.findDepartmentByDeptName(department.getDeptName());

            if (departmentOptional.isPresent()) {
                instructor.setDepartment(departmentOptional.get());
                return instructorRepository.save(instructor);
            } else {
                throw new NotFoundException("No existing department found as given department name");
            }
        }
    }

    public Instructor getInstructorById(Long instructorId) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);

        if (instructorOptional.isEmpty()) {
            throw new NotFoundException("Instructor with id " + instructorId + " doesn't exists");
        } else {
            return instructorOptional.get();
        }
    }

    public void deleteInstructor(Long instructorId) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);

        if (instructorOptional.isPresent()) {
            instructorRepository.deleteById(instructorId);
        } else {
            throw new NotFoundException("Instructor with id " + instructorId + " doesn't exists");
        }
    }

    public Instructor updateInstructor(Instructor instructor) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructor.getId());

        if (instructorOptional.isPresent()) {
            Instructor ins = instructorOptional.get();
            String name = instructor.getName();
            String email = instructor.getEmail();
            String designation = instructor.getDesignation();
            Long salary = instructor.getSalary();
            Department department = instructor.getDepartment();

            if (name != null && name.length() > 0) {
                ins.setName(name);
            }

            if (email != null && email.length() > 0 && !Objects.equals(email, ins.getEmail())) {
                Optional<Instructor> emailExists = instructorRepository.findInstructorsByEmail(email);

                if (emailExists.isPresent()) {
                    throw new AlreadyExistsException("An instructor already exists with this email account");
                } else {
                    ins.setEmail(email);
                }
            }

            if (designation != null && designation.length() > 0) {
                ins.setDesignation(designation);
            }

            if (salary != null) {
                ins.setSalary(salary);
            }

            if (department != null) {
                Optional<Department> departmentOptional = departmentRepository.findDepartmentByDeptName(department.getDeptName());

                if (departmentOptional.isPresent()) {
                    ins.setDepartment(departmentOptional.get());
                    return instructorRepository.save(ins);
                } else {
                    throw new NotFoundException("No existing department found as given department name");
                }
            }

            return instructorRepository.save(ins);
        } else {
            throw new NotFoundException("Instructor with id " + instructor.getId() + " doesn't exists");
        }
    }

    public Instructor assignSections(Long instructorId, String subjectCode, Integer year, Integer term) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);

        if (instructorOptional.isEmpty()) {
            throw new NotFoundException("Student with id " + instructorId + " doesn't exists");
        } else {
            Instructor inst = instructorOptional.get();
            SectionSerializable sectionSerializable = new SectionSerializable(subjectCode, year, term);
            Optional<Section> sectionOptional = sectionRepository.findById(sectionSerializable);

            if (sectionOptional.isEmpty()) {
                throw new NotFoundException("The following Id doesn't belong to any section : " + sectionSerializable);
            }
            Set<Section> assignedSectionSet = inst.getSections();
            assignedSectionSet.add(sectionOptional.get());
            inst.setSections(assignedSectionSet);

            return instructorRepository.save(inst);
        }
    }
}
