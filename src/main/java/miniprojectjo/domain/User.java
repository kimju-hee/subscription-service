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

    private String password; // 비밀번호 필드 추가

    private boolean isPurchase = false;

    private int point = 0; // 포인트 필드 추가

    private String message;

    public void suggestFeeConversion(){
        // TODO: 비즈니스 로직 구현
        // 예를 들어, 사용자에게 이메일이나 알림을 보내는 로직을 추가할 수 있습니다。
        this.setMessage("포인트 부족으로 구독에 실패했습니다. 유료 구독으로 전환하시겠습니까?");
    }


    // 포인트 레파지토리의 포인트차감 파트와 겹치는 부분이 있어 현재 미사용!
    
    // 도서에 따른 구독료를 받아와서 포인트 차감하는 함수
    // 해당 함수 호출 시, 시스템으로부터 전달받은 구독료를 인자로 전달함
    public void deductPoint(int amount) throws InsufficientPointsException {
        if (this.point < amount) {
            OutOfPoint outOfPoint = new OutOfPoint();
            outOfPoint.setUserId(this.id);
            outOfPoint.setPoint(this.point); // 현재 유저의 포인트
            outOfPoint.publishAfterCommit();
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }   
        // 포인트가 부족하면 outofpoint 이벤트 발행 및 InsufficientPointsException를 발생 -> 호출한 함수에 포인트 부족 상황을 리턴함
        this.point -= amount;   // point 에서 구독료만큼 빼서 저장
    }

}
