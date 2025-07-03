// RegisterUserCommand.java
package miniprojectjo.domain;

import lombok.Data;

@Data
public class RegisterUserCommand {
    private String email;
    private String userName;
    private String password;
}

