package miniprojectjo.external;

import lombok.Data;

@Data
public class SubscriptionQuery {
    private Long userId;
    private Long bookId;
    private String reason;
}
