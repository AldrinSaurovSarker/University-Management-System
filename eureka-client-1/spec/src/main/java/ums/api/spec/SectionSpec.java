package ums.api.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ums.api.entities.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionSpec {
    public static Specification<Section> getSections(
            String subjectCode, Integer year, Integer term
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if (subjectCode != null && !(subjectCode.isEmpty())) {
                predicate.add(criteriaBuilder.like(root.get("subjectCode"), "%"+subjectCode+"%"));
            }

            if (year != null) {
                predicate.add(criteriaBuilder.equal(root.get("year"), year));
            }

            if (term != null) {
                predicate.add(criteriaBuilder.equal(root.get("term"), term));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        });
    }
}
