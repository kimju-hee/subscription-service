package miniprojectjo.external;

import org.springframework.stereotype.Service;

@Service
public class ExternalSubscriptionService  {
    public boolean subscription(SubscriptionQuery query) {
        // 실제 외부 시스템 연동이 있다면 여기에 구현
        System.out.println("❗ 외부 서비스 호출됨: " + query);
        return true;
    }
}
