package miniprojectjo.infra;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Long userId;
    private Long bookId;
    private int subscriptionFee; // 구독료 필드 추가
    // private String webUrl; // 선택적으로 필요
}
