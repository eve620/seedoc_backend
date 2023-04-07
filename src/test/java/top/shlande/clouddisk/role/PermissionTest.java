package top.shlande.clouddisk.role;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class PermissionTest {
    private static final Permission readAll = new WildcardPermission("read:*");
    private static final Permission writeAll = new WildcardPermission("write:*");
    private static final Permission writeDir = new WildcardPermission("write:dir/*");

    @Test
    public void test() {
        var writeTo =new WildcardPermission("write:somedir");
        Assert.isTrue(writeAll.implies(writeTo),"should be writeable");
        Assert.isTrue(!writeDir.implies(writeTo),"should not be writeable");
    }
}
