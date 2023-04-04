package top.shlande.clouddisk.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.user.SimpleLoginService;
import top.shlande.clouddisk.user.UserRole;
import top.shlande.clouddisk.user.UserService;

import java.util.Arrays;

@RequestMapping("user")
public class UserController {
    private final static String CookieKeyUserId = "userId";
    private final UserService userService;
    private final SimpleLoginService loginService;

    public UserController(@Autowired UserService userService, @Autowired SimpleLoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    public static class LoginRequest {
        public String name;
        public String password;
    }

    // 用户退出
    @DeleteMapping("/logout")
    public void logout(HttpServletResponse response) {
        // https://kodejava.org/how-do-i-delete-a-cookie-in-servlet/
        var cookie = new Cookie(CookieKeyUserId, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    // 用户登录
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest login, HttpServletResponse response) {
        response.addCookie(new Cookie(CookieKeyUserId, this.loginService.login(login.name, login.password)));
    }

    public static class UserRequest {
        public String name;
        public String group;
        public UserRole role;
        public String context;
    }

    // 添加用户
    @PostMapping("/user")
    public String create(@RequestBody UserRequest request, HttpServletRequest http) {
        // TODO: 使用filter进行拦截，保证一定有cookie
        // TODO:get userid from http
        return this.userService.addUser(getUserId(http), request.name, request.group, request.role).id;
    }

    // 删除用户
    @DeleteMapping("/user/{userId}")
    public void delete(@PathVariable String userId, HttpServletRequest http) {
        // TODO:get userid from http
        String operator = "get userid from http";
        this.userService.deleteUser(operator, userId);
    }

    // 设置用户信息
    // operator name context
    @PutMapping("/user/{userId}")
    public void setUser(@PathVariable String userId, HttpServletRequest http, @RequestBody UserRequest request) {
        this.userService.setUser(getUserId(http), userId, request.name, request.context, request.group, request.role);
    }

    // 添加用户组
    public static class GroupRequest {
        public String name;
        public String context;
    }

    @PostMapping("/group")
    public String createGroup(@RequestBody GroupRequest group, HttpServletRequest http) {
        return this.userService.addGroup(getUserId(http), group.name, group.context);
    }

    @PutMapping("/group/{groupId}")
    public void setGroup(@PathVariable String groupId, @RequestBody GroupRequest request, HttpServletRequest http) {
        this.userService.setGroup(getUserId(http), groupId, request.name, request.context);
    }

    @DeleteMapping("/group/{groupId}")
    public void deleteGroup(@PathVariable String groupId, HttpServletRequest http) {
        this.userService.deleteGroup(getUserId(http), groupId);
    }

    private String getUserId(HttpServletRequest http) {
        return Arrays.stream(http.getCookies())
                .filter(cookie -> cookie.getName().equals(CookieKeyUserId))
                .findFirst().get().getValue();
    }
}
