package miniprojectjo.domain;

import java.util.Date;
import lombok.Data;

@Data
public class GetSubscriptionQuery {

    private Long id;
    private Boolean isSubscription;
    private Date startSubscription;
    private Date endSubscription;
    private String webUrl;
    private BookId bookId;
    private UserId userId;
}
