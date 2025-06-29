package miniprojectjo.domain;
import java.util.Optional;
import java.util.Date;
import java.util.List;
import miniprojectjo.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import feign.Param;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "subscriptions",
    path = "subscriptions"
)
public interface SubscriptionRepository
    extends PagingAndSortingRepository<Subscription, Long> {
    @Query(
        value = "select subscription " +
        "from Subscription subscription " +
        "where(:id is null or subscription.id = :id) and (subscription.isSubscription = :isSubscription) and (:startSubscription is null or subscription.startSubscription = :startSubscription) and (:endSubscription is null or subscription.endSubscription = :endSubscription) and (:webUrl is null or subscription.webUrl like %:webUrl%) and (:bookId is null or subscription.bookId = :bookId) and (:userId is null or subscription.userId = :userId)"
    )
    Optional<Subscription> getSubscription(
        @Param("id") Long id,
        @Param("isSubscription") Boolean isSubscription,
        @Param("startSubscription") Date startSubscription,
        @Param("endSubscription") Date endSubscription,
        @Param("webUrl") String webUrl,
        @Param("bookId") BookId bookId,
        @Param("userId") UserId userId
    );
}
