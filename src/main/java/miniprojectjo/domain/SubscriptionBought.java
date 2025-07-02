package miniprojectjo.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionBought extends AbstractEvent {

    private Long id;
    private Boolean isPurchase;

    public SubscriptionBought(User aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.isPurchase = aggregate.getIsPurchase(); 
    }

    public SubscriptionBought() {
        super();
    }
}
