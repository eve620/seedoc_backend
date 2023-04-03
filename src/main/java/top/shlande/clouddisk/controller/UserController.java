package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("user")
public class UserController {

    public static class LoginRequest {
        public String name;
        public String password;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest login, HttpServletResponse response) {

    }

}
