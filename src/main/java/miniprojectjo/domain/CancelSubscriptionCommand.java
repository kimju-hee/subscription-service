package miniprojectjo.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class CancelSubscriptionCommand {
    private Long userId;
    private Long bookId;
}
