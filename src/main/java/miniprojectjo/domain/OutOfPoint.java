package miniprojectjo.domain;

import java.util.*;
import lombok.*;
import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

@Data
@ToString
public class OutOfPoint extends AbstractEvent {

    private Long id;
    private Integer point;
    private Object userId;
    private Object subscriptionId;
}
