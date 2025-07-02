package miniprojectjo.domain;

import miniprojectjo.domain.SubscriptionApplied;
import miniprojectjo.domain.SubscriptionFailed;
import miniprojectjo.SubscriberApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;


@Entity     //  JPA가 관리하는 DB 테이블 객체
@Table(name="Subscription_table")
@Data   //  Lombok이 getter/setter, toString, equals 등을 자동 생성
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean isSubscription;

    private Date startSubscription;
    private Date endSubscription;

    private String webUrl;

    @Embedded
    private BookId bookId;

    @Embedded
    private UserId userId;     //  구독 대상 도서 ID와 사용자 ID

    // 도메인 중심 메서드

    // ✅ 구독 시작
    public void apply() {
        this.isSubscription = true;     // 상태를 true 로 전환
        this.startSubscription = new Date();  // 시작일 설정
        this.endSubscription = calculateEndDate();  // 종료일 설정

        SubscriptionApplied event = new SubscriptionApplied(this);
        event.publishAfterCommit();   // 이벤트 SubscriptionApplied를 생성하고 Kafka에 발행
    }

    // ✅ 구독 취소
    public void cancel() {
        this.isSubscription = false;    // 상태를 false 로 전환
        this.endSubscription = new Date();  // 종료일 설정

        SubscriptionCanceled event = new SubscriptionCanceled(this);
        event.publishAfterCommit();   // 이벤트 SubscriptionCanceled를 생성하고 Kafka에 발행
    }

    // ✅ 구독 실패 (정적 메서드 → 리팩토링)
    public void fail(OutOfPoint outOfPoint) {
        this.isSubscription = false;   // 상태를 false로 설정
        this.endSubscription = new Date();  // 종료일 설정

        SubscriptionFailed failed = new SubscriptionFailed(this, "포인트 부족");
        failed.publishAfterCommit();
    }
    // 종료일 계산 함수
    // 예시: 종료일은 시작일 기준 30일 뒤
    private Date calculateEndDate() {
        long duration = 1000L * 60 * 60 * 24 * 30;
        return new Date(System.currentTimeMillis() + duration);
    }

    // ✅ Repository 접근 헬퍼
    public static SubscriptionRepository repository() {
        return SubscriberApplication.applicationContext.getBean(SubscriptionRepository.class);
    }
}
