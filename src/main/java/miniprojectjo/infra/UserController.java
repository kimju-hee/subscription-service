package miniprojectjo.infra;

import javax.transaction.Transactional;
import miniprojectjo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users/register")
    public User registerUser(@RequestBody RegisterUserCommand command) {
        User user = new User();
        user.setEmail(command.getEmail());
        user.setUserName(command.getUserName());
        user.setPassword(command.getPassword()); // 비밀번호 설정
        user.setPurchase(false); // 기본값
        user.setMessage("회원가입 완료");

        userRepository.save(user);

        // 이벤트 발행
        UserRegistered userRegistered = new UserRegistered(user);
        userRegistered.publishAfterCommit();

        return user;
    }

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @PutMapping("/users/buy-subscription")
    public User buySubscription(@RequestBody BuySubscriptionCommand command) {
        // 유저 조회
        User user = userRepository.findById(command.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        // 이미 구독 중이면 예외 처리
        if (user.isPurchase()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 구독권 사용 중입니다.");
        }

        // User의 isPurchase 상태 업데이트
        user.setPurchase(true);
        user.setMessage("구독권 구매 완료");        
        userRepository.save(user);

        // SubscriptionBought 이벤트 발행
        SubscriptionBought subscriptionBought = new SubscriptionBought(command.getUserId(), user.isPurchase());
        subscriptionBought.publishAfterCommit();

        return user;
    }
}
