package miniprojectjo.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniprojectjo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class SubscriptionController {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    // 구독 신청 API
    @PostMapping("/subscriptions/buy")
    public Subscription buySubscription(@RequestBody BuySubscriptionCommand command) {
        Subscription subscription = new Subscription();
        subscription.setUserId(command.getUserId());
        subscription.setBookId(command.getBookId());

        int userPoint = 10000; // 예시용. 추후 외부 시스템 연동 필요
        int subscriptionCost = 5000;

        subscription.apply(userPoint, subscriptionCost);
        return subscriptionRepository.save(subscription);
    }

    // 구독 취소 API
    @RequestMapping(
        value = "/subscriptions/{id}/cancelsubscription",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription cancelSubscription(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /subscription/cancelSubscription  called #####");
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);

        optionalSubscription.orElseThrow(() -> new Exception("No Entity Found"));
        Subscription subscription = optionalSubscription.get();
        subscription.cancelSubscription();

        subscriptionRepository.save(subscription);
        return subscription;
    }
}
