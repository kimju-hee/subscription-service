package miniprojectjo.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class UserRegistered extends AbstractEvent {

    private Long id;
    private String email;
    private String userName;

    public UserRegistered(User aggregate) {
        super(aggregate);
    }

    public UserRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
