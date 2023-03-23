package ums.api.entities;

import lombok.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SectionSerializable implements Serializable {
    private String subjectCode;
    private Integer year;
    private Integer term;
}
