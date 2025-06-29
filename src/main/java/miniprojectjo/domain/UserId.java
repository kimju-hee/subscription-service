package miniprojectjo.domain;

import javax.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserId {
    private Long value;
}
