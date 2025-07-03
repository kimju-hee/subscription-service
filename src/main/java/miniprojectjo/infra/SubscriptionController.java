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

    @Autowired
    UserRepository userRepository;

    // 도메인 로직을 직접 수행하지 않고, DB 엔티티와 상호작용함

// ✅ [1] 구독 신청 API
    @PostMapping
    public Subscription applySubscription(@RequestBody SubscriptionRequest request) throws Exception {
        UserId userId = new UserId(request.getUserId());
        BookId bookId = new BookId(request.getBookId());

        // 사용자 조회
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        // 이미 구독한 경우 예외
        if (subscriptionRepository.existsByUserIdAndBookIdAndIsSubscription(userId, bookId, true)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "이미 해당 도서를 구독 중입니다."
            );
        }

        // 구독권 유무에 따른 처리
        if (!user.isPurchase()) {
            // 구독권이 없는 경우, 포인트를 소비하여 도서를 구독하는 기능 (틀만 구현)
            // 1. Subscription 객체를 먼저 생성하고 저장하여 ID를 확보
            Subscription subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setBookId(bookId);
            // subscription.apply(); // 포인트 차감 성공 시에만 apply 호출
            subscriptionRepository.save(subscription); // 대기 상태로 저장
        }

        // 신규 구독 처리 (구독권이 있는 경우)
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setBookId(bookId);
        subscription.apply(); // 도메인 로직

        return subscriptionRepository.save(subscription);
    }

// ✅ [2] 구독 취소 API (리팩토링)
    @PutMapping("/{id}/cancel")
    public Subscription cancelSubscription(@PathVariable Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "해당 구독을 찾을 수 없습니다.")
            );

        subscription.cancel(); // 도메인 메서드에서 이벤트 발행 포함
        return subscriptionRepository.save(subscription);
    }
}
//>>> Clean Arch / Inbound Adaptor



