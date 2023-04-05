package top.shlande.clouddisk.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    private final String superadminId = "admin";

    @Test
    public void test() {
        // 超级管理员创建分组
        var testGroupName = "测试分组";
        var testGroupContext = "test";
        var testGroupId = this.userService.addGroup(superadminId, testGroupName, testGroupContext);

        // 超级管理员添加普通管理员
        var adminUserName = "测试管理员";
        var adminUserRole = UserRole.ADMIN;
        String adminUserContext = null;
        var admin = this.userService.addUser(superadminId, adminUserName, testGroupId, adminUserRole);

        // 普通管理员添加普通用户
        var normalUserName = "测试用户";
        var normalUserRole = UserRole.USER;
        String normalUserContext = null;
        var user = this.userService.addUser(admin.id, normalUserName, null, normalUserRole);

        // 普通用户创建用户，应该失败
        var gotError = false;
        try {
            this.userService.addUser(user.id, "shouldNotSuccessAdded", null, normalUserRole);
        } catch (DenyException e) {
            gotError = true;
        }
        Assert.isTrue(gotError, "should got error");
        // 普通用户创建管理员
        gotError = false;
        try {
            this.userService.addUser(user.id, "shouldNotSuccessAdded", null, adminUserRole);
        } catch (DenyException e) {
            gotError = true;
        }
        Assert.isTrue(gotError, "should got error");
        // 普通用户修改自己信息
        gotError = false;
        try {
            this.userService.setUser(user.id, user.id, "shouldNotSuccessAdded", null, testGroupId, UserRole.ADMIN);
        } catch (Exception e) {
            gotError = true;
        }
        Assert.isTrue(gotError, "should got error");

        // 普通管理员删除用户
        this.userService.deleteUser(admin.id, user.id);
        // 普通管理员创建管理员
        this.userService.addUser(admin.id, "shouldNotSuccessAdded", null, UserRole.ADMIN);
        // 普通管理员删除管理员
        gotError = false;
        try {
            this.userService.deleteUser(admin.id, admin.id);
        } catch (DenyException e) {
            gotError = true;
        }
        Assert.isTrue(gotError, "should got error");
        // 超级管理员删除管理员
        this.userService.deleteUser(superadminId, admin.id);
    }
}
