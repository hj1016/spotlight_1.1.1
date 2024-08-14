package spotlight.spotlight_ver2.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spotlight.spotlight_ver2.dto.UserRegistrationDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 새로운 유저 등록
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
//    }
}
