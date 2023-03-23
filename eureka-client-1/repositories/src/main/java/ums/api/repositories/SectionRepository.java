package ums.api.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ums.api.entities.Section;
import ums.api.entities.SectionSerializable;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, SectionSerializable> {
    List<Section> findAll(Specification<Section> sectionSpecification);
}
