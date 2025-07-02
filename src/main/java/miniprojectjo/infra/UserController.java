package miniprojectjo.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniprojectjo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/users")
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users/register")
        public User registerUser(@RequestBody RegisterUserCommand command) {
        return User.register(command); 
    }

    @RequestMapping(
        value = "/users/buysubscription/{Id}",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public User buySubscription(
        @PathVariable("Id") Long Id,
        HttpServletRequest request,
        HttpServletResponse response
        ) {
        // 유저 조회
        User user = userRepository.findById(Id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        // 이미 구독 중이면 예외 처리
        if (Boolean.TRUE.equals(user.getIsPurchase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 구독권 사용 중입니다.");
        }

        // 구독 로직 수행 (도메인 로직에서 이벤트 발행 포함)
        user.buySubscription();

        // 저장 및 반환
        return userRepository.save(user);
    }
}
//>>> Clean Arch / Inbound Adaptor

