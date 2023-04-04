package top.shlande.clouddisk.user;


import org.junit.jupiter.api.Test;

public class UserServiceTest {
    private UserService userService;
    private final String adminId = "admin";

    @Test
    public void test() {
        var testGroupName = "test";
        var testGroupContext = "test";
        var testGroupId = this.userService.addGroup(adminId, testGroupName, testGroupContext);

        var normalUserName = "user";
        var normalUserRole = UserRole.User;
        String normalUserContext = null;
        this.userService.addUser(adminId, normalUserName, testGroupId, normalUserRole);
    }
}
