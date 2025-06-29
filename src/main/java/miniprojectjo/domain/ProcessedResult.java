package miniprojectjo.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class ProcessedResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private Long bookId;
    private String status; // 예: "CANCELED", "ACTIVE"
    private Date updatedAt;
}
