package miniprojectjo.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import miniprojectjo.domain.*;
import miniprojectjo.infra.SubscriptionRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


//<<< Clean Arch / Inbound Adaptor
// 외부 요청(HTTP API)를 받아 도메인 계층에 전달

@RestController
// Spring REST API 컨트롤러. JSON 반환 자동 처리
@RequestMapping(value="/subscriptions")

@Transactional
// 컨트롤러 메서드가 실행되는 동안 하나의 트랜잭션으로 처리됨
public class SubscriptionController {

    @Autowired
    // 의존성 주입
    SubscriptionRepository subscriptionRepository;
    // 도메인 로직을 직접 수행하지 않고, DB 엔티티와 상호작용함

// ✅ [1] 구독 신청 API
    @PostMapping
    public Subscription applySubscription(@RequestBody SubscriptionRequest request) throws Exception {
        UserId userId = new UserId(request.getUserId());
        BookId bookId = new BookId(request.getBookId());

    // ✅ 이미 구독한 경우 예외
    if (subscriptionRepository.existsByUserIdAndBookIdAndIsSubscription(userId, bookId, true)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "이미 해당 도서를 구독 중입니다."
        );
    }


    // ✅ 신규 구독 처리
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setBookId(bookId);
        subscription.apply(); // 도메인 로직

        return subscriptionRepository.save(subscription);
    }

// ✅ [2] 구독 취소 API (리팩토링)
    @PutMapping("/{id}/cancel")
    public Subscription cancelSubscription(@PathVariable Long id) throws Exception {
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "해당 구독을 찾을 수 없습니다.")
            );

        subscription.cancel(); // 도메인 메서드에서 이벤트 발행 포함
        return subscriptionRepository.save(subscription);
    }
}
//>>> Clean Arch / Inbound Adaptor
