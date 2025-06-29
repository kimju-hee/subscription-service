package miniprojectjo.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
public class SubscriptionCanceled extends AbstractEvent {

    private Long id;
    private Boolean isSubscription;
    private Date endSubscription;

    private Long bookId;
    private Long userId;

    public SubscriptionCanceled(Subscription aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.isSubscription = aggregate.getIsSubscription();
        this.endSubscription = aggregate.getEndSubscription();
        if (aggregate.getBookId() != null)
            this.bookId = aggregate.getBookId().getValue();
        if (aggregate.getUserId() != null)
            this.userId = aggregate.getUserId().getValue();
    }

}
//>>> DDD / Domain Event
