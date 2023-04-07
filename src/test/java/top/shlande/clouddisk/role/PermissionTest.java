package top.shlande.clouddisk.role;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class PermissionTest {
    @Autowired
    private SecurityManager manager;

    private static final Permission readAll = new WildcardPermission("read:*");
    private static final Permission writeAll = new WildcardPermission("write:*");
    private static final Permission writeDir = new WildcardPermission("write:dir/*");
    @Test
    public void test() {
        var writeTo =new WildcardPermission("write:somedir");
        Assert.isTrue(writeAll.implies(writeTo),"should be writeable");
        Assert.isTrue(!writeDir.implies(writeTo),"should not be writeable");
    }

    @Test
    public void testApp() {
        SecurityUtils.setSecurityManager(manager);
        var subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("admin","admin"));
        subject.getPrincipal();
    }
}
