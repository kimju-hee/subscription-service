package miniprojectjo.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionCanceled extends AbstractEvent {

    private Long id;
    private Boolean isSubscription;

    public SubscriptionCanceled(Subscription aggregate) {
        super(aggregate);
    }

    public SubscriptionCanceled() {
        super();
    }
}
//>>> DDD / Domain Event
