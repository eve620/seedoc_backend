package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.entity.Permissions;
import top.shlande.clouddisk.user.UserService;

import java.util.Objects;

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
    public void login(@RequestBody LoginRequest login) {
        var subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(login.name, login.password));
    }

    public static class UserRequest {
        enum Role {
            admin,
            user
        }

        public String name;
        public String password;
        public Role role;
        public String permission;

        public SimpleRole getRole() {
            if (Objects.requireNonNull(this.role) == Role.admin) {
                return Permissions.admin;
            }
            return Permissions.user;
        }
    }

    // 添加用户
    @PostMapping("/user")
    public String create(@RequestBody UserRequest request, HttpServletRequest http) {
        // TODO: 使用filter进行拦截，保证一定有cookie
        // TODO:get userid from http
        return this.userService.addUser(getUserId(http), request.name, request.password, request.getRole(), new WildcardPermission(request.permission)).id;
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
        this.userService.setUser(getUserId(http), userId, request.name, new WildcardPermission(request.permission), request.getRole());
    }

    // 添加用户组
    public static class GroupRequest {
        public String name;
        public String context;
    }

    private String getUserId(HttpServletRequest http) {
        var session = http.getSession(false);
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("userId");
    }
}
