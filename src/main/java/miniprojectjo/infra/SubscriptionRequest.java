package miniprojectjo.infra;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Long userId;
    private Long bookId;
    // private String webUrl; // 선택적으로 필요
}
