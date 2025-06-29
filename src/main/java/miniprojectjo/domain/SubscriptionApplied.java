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
public class SubscriptionApplied extends AbstractEvent {

    private Long id;
    private Boolean isSubscription;
    private Date startSubscription;
    private Date endSubscription;
    private String webUrl;

    private Long bookId;
    private Long userId;

    public SubscriptionApplied(Subscription aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.isSubscription = aggregate.getIsSubscription();
        this.startSubscription = aggregate.getStartSubscription();
        this.endSubscription= aggregate.getEndSubscription();
        this.webUrl = aggregate.getWebUrl();

        if (aggregate.getBookId() != null)
            this.bookId = aggregate.getBookId().getValue();
        if (aggregate.getUserId() != null)
            this.userId = aggregate.getUserId().getValue();
    }
}
//>>> DDD / Domain Event