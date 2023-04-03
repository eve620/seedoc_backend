package top.shlande.clouddisk.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.user.SimpleLoginService;
import top.shlande.clouddisk.user.UserDetailService;
import top.shlande.clouddisk.user.UserRole;
import top.shlande.clouddisk.user.UserService;

@RequestMapping("user")
public class UserController {
    private UserService userService;
    private SimpleLoginService loginService;

    public static class LoginRequest {
        public String name;
        public String password;
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletResponse response) {
        response.
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest login, HttpServletResponse response) {
        var token = this.loginService.login(login.name, login.password);
        response.addCookie(new Cookie("token", token));
    }

    public static class CreateUserRequest {
        public String name;
        public String group;
        public UserRole role;
        public String context;
    }

    // 添加用户
    @PostMapping
    public String create(@RequestBody CreateUserRequest request, HttpServletRequest http) {
        // TODO:get userid from http
        String operator = "get userid from http";
        return this.userService.addUser(operator, request.name, request.group, request.role).id;
    }

    // 删除用户
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable String userId, HttpServletRequest http) {
        // TODO:get userid from http
        String operator = "get userid from http";
        this.userService.deleteUser(operator, userId);
    }

}
