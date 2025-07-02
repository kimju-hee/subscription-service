package miniprojectjo.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import miniprojectjo.SubscriberApplication;
import miniprojectjo.domain.UserRegistered;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String userName;

    private Boolean isPurchase = false;

    private String meseeage;

    public Long getId() {
        return this.id;
    }

    // 회원가입 구현

    public static User register(RegisterUserCommand command) {
        User user = new User();
        user.setEmail(command.getEmail());
        user.setUserName(command.getUserName());
        user.setIsPurchase(false); // 기본값
        user.setMeseeage("회원가입 완료");

        repository().save(user);

        // 이벤트 발행
        UserRegistered userRegistered = new UserRegistered(user);
        userRegistered.publishAfterCommit();

        return user;
    }

    public static UserRepository repository() {
        UserRepository userRepository = SubscriberApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public void buySubscription() {
        this.isPurchase = true;

        SubscriptionBought event = new SubscriptionBought(this);
        event.publishAfterCommit();
    }

    public static void guideFeeConversionSuggestion(SubscriptionFailed subscriptionFailed){}
    // User.java

}
