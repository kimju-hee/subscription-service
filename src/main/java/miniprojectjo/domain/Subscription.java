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


@Entity
@Table(name="Subscription_table")
@Data

//<<< DDD / Aggregate Root
public class Subscription  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
private Long id;    
    
    
private Boolean isSubscription;    
    
    
private Date startSubscription;    
    
    
private Date endSubscription;    
    
    
private String webUrl;    
    
    @Embedded
private BookId bookId;    
    
    @Embedded
private UserId userId;

    // 구독 신청 도메인 행위
    public void apply() {
        if (Boolean.TRUE.equals(this.isSubscription)) {
            throw new IllegalStateException("이미 구독 중입니다.");
            // 이미 구독 중일 경우, 구독 신청하지 못하도록 막음     
        }

        if (point < cost) {
            this.fail();
            return;      // 포인트가 부족할 경우, 구독 실패함
        }
        // 아닐 경우, 정상적으로 구독 완료

        this.isSubscription = true;
        this.startSubscription = new Date();      // 구독 시작일

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.startSubscription);
        cal.add(Calendar.DATE, 30); 
        this.endSubscription = cal.getTime();  // 종료일은 구독 시작일로부터 30일 뒤로 설정

        new SubscriptionApplied(this).publishAfterCommit();   // 구독 신청 이벤트 발행
    }

    // 구독 취소 도메인 행위
    public void cancel() {
        if (!Boolean.TRUE.equals(this.isSubscription)) {
            throw new IllegalStateException("구독 상태가 아닙니다.");
            // 구독 중이 아닐 경우, 구독 취소하지 못하도록 막음 
        }

        this.isSubscription = false;    // 구독 여부를 false로 변경
        this.endSubscription = new Date();   // 종료일을 현재로 변경

        new SubscriptionCanceled(this).publishAfterCommit();    // 구독 취소 이벤트 발행
    }

    // 구독 실패 도메인 행위
    public void fail() {
        this.isSubscription = false;   // 구독 여부를 false로 변경
        new SubscriptionFailed(this).publishAfterCommit();     // 구독 실패 이벤트 발행
    }

    @PostPersist
    public void onPostPersist() {
        // 이미 apply()에서 SubscriptionApplied 이벤트를 명시적으로 발행하고 있으므로, 생략 가능
    }

    public static SubscriptionRepository repository() {
        return SubscriberApplication.applicationContext.getBean(SubscriptionRepository.class);
    }
}


}
//>>> DDD / Aggregate Root
