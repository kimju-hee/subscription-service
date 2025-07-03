package miniprojectjo.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionBought extends AbstractEvent {

    private Long userId;
    private boolean isPurchase;

    public SubscriptionBought(Long userId, boolean isPurchase) {
        this.userId = userId;
        this.isPurchase = isPurchase;
    }

    public SubscriptionBought() {
        super();
    }
}
