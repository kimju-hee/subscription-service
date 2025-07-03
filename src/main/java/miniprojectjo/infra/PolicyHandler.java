package miniprojectjo.infra;

import javax.transaction.Transactional;
import miniprojectjo.config.kafka.KafkaProcessor;
import miniprojectjo.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    private static final Logger logger = LoggerFactory.getLogger(PolicyHandler.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OutOfPoint'"
    )
    public void wheneverOutOfPoint_FailSubscription(
        @Payload OutOfPoint outOfPoint
    ) {
        logger.info("##### listener FailSubscription : {}", outOfPoint);

        subscriptionRepository.findById(outOfPoint.getSubscriptionId())
            .ifPresent(subscription -> {
                userRepository.findById(subscription.getUserId().getId())
                    .ifPresent(user -> {
                        if (user.isPurchase()) {
                            logger.warn("##### OutOfPoint event received for user with purchase pass. Subscription ID: {}, User ID: {}. Not failing subscription.", subscription.getId(), user.getId());
                        } else {
                            subscription.fail("포인트 부족");
                            subscriptionRepository.save(subscription);
                        }
                    });
            });
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionFailed'"
    )
    public void wheneverSubscriptionFailed_GuideFeeConversionSuggestion(
        @Payload SubscriptionFailed subscriptionFailed
    ) {
        logger.info("##### listener GuideFeeConversionSuggestion : {}", subscriptionFailed);

        userRepository.findById(subscriptionFailed.getUserId())
            .ifPresent(user -> {
                user.suggestFeeConversion();
                userRepository.save(user);
            });
    }

    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionCanceled'")
    public void onSubscriptionCanceled(@Payload SubscriptionCanceled event) {
        if (!event.validate()) return;

        logger.info("SubscriptionCanceled 이벤트 수신됨: {}", event);

        // 예시: 로그 남기기 or 사용자/구독 상태를 업데이트
        // 추후 필요한 비즈니스 로직 연결 가능
    }

    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionBought'")
    public void wheneverSubscriptionBought_UpdateUserPurchaseStatus(@Payload SubscriptionBought subscriptionBought) {
        logger.info("##### listener UpdateUserPurchaseStatus : {}", subscriptionBought);

        userRepository.findById(subscriptionBought.getUserId())
            .ifPresent(user -> {
                user.setPurchase(subscriptionBought.isPurchase());
                userRepository.save(user);
            });
    }
}

