package miniprojectjo.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalSubscriptionService {

    @Value("${external.subscription.url}") // application.yml에서 주입받음
    private String subscriptionApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean subscription(SubscriptionQuery query) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SubscriptionQuery> request = new HttpEntity<>(query, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                subscriptionApiUrl,
                request,
                String.class
            );

            // 성공 여부 확인 (예: HTTP 2xx 응답)
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ 외부 구독 API 호출 성공");
                return true;
            } else {
                System.out.println("⚠️ 외부 구독 API 호출 실패: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ 외부 구독 API 예외 발생: " + e.getMessage());
            return false;
        }
    }
}   