package miniprojectjo.infra;

import miniprojectjo.config.kafka.KafkaProcessor;
import miniprojectjo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.handler.annotation.Payload;

@Service
@Transactional
public class ProcessedResultViewHandler {

    @Autowired
    ProcessedResultRepository processedResultRepository;

    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionCanceled'")
    public void onSubscriptionCanceled(@Payload SubscriptionCanceled event) {
        if (!event.validate()) return;

        processedResultRepository
            .findByUserIdAndBookId(event.getUserId(), event.getBookId())
            .ifPresentOrElse(
                result -> {
                    result.setStatus("CANCELED");
                    result.setUpdatedAt(event.getEndSubscription());
                    processedResultRepository.save(result);
                },
                () -> {
                    // 없으면 새로 만들기
                    ProcessedResult result = new ProcessedResult();
                    result.setUserId(event.getUserId());
                    result.setBookId(event.getBookId());
                    result.setStatus("CANCELED");
                    result.setUpdatedAt(event.getEndSubscription());
                    processedResultRepository.save(result);
                }
            );
    }
}
