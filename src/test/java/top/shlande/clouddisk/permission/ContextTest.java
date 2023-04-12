package top.shlande.clouddisk.permission;


import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import top.shlande.clouddisk.entity.UserContext;

public class ContextTest {
    @Test
    public void test() {
        var root = new UserContext("电子信息工程/");
        Assert.isTrue(root.canAccess("电子信息工程/Screenshot 2023-03-26 at 18.18.41.png"),"should allowed to access");
    }
}
