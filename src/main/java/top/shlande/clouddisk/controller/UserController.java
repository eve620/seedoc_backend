package top.shlande.clouddisk.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.entity.User;
import top.shlande.clouddisk.entity.UserContext;
import top.shlande.clouddisk.user.NotFoundException;
import top.shlande.clouddisk.user.UserService;

@RestController
@RequestMapping("user")
public class UserController {
    private final static String CookieKeyUserId = "userId";
    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @Data
    public static class LoginRequest {
        public String name;
        public String password;
    }

    // 用户退出
    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        // https://kodejava.org/how-do-i-delete-a-cookie-in-servlet/
        var session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.invalidate();
    }


    // 用户登录
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
        if (!this.userService.login(login.name, login.password)) {
            throw new NotFoundException(login.name);
        }
        var session = request.getSession(true);
        session.setAttribute("userId", login.name);
        var cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    @GetMapping("/whoami")
    public UserInfo whoami(HttpServletRequest request) {
        var user = this.userService.user(getUserId(request));
        return new UserInfo(user);
    }

    // 获取用户信息
    @GetMapping("/{id}")
    public UserInfo info(@PathVariable("id") String id) {
        return new UserInfo(this.userService.user(id));
    }

    @Data
    @NoArgsConstructor
    public static class UserInfo {
        public String id;
        public String name;
        public User.Role role;
        public String permission;

        public UserInfo(User user) {
            this.name = user.name;
            this.role = user.role;
            this.permission = user.context.toString();
        }
    }

    public static class UserRequest {
        public String name;
        public String password;
        public User.Role role;
        public String permission;
    }

    // 添加用户
    @PostMapping("/user")
    public String create(@RequestBody UserRequest request, HttpServletRequest http) {
        // TODO: 使用filter进行拦截，保证一定有cookie
        // TODO:get userid from http
        return this.userService.addUser(getUserId(http), request.name, request.password, request.role, new UserContext(request.permission)).id;
    }

    // 删除用户
    @DeleteMapping("/user/{userId}")
    public void delete(@PathVariable String userId, HttpServletRequest http) {
        this.userService.deleteUser(getUserId(http), userId);
    }

    // 设置用户信息
    // operator name context
    @PutMapping("/user/{userId}")
    public void setUser(@PathVariable String userId, HttpServletRequest http, @RequestBody UserRequest request) {
        this.userService.setUser(getUserId(http), userId, request.name, new UserContext(request.permission), request.role);
    }

    private String getUserId(HttpServletRequest http) {
        var session = http.getSession(false);
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("userId");
    }
}
