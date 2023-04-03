package top.shlande.clouddisk.role;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import top.shlande.clouddisk.user.UserContext;

import java.util.List;
import java.util.Objects;

public class UserContextTest {
    private final UserContext root = new UserContext(List.of(""));
    private final UserContext sub = new UserContext(List.of("sub"));
    private final UserContext multi = new UserContext(List.of("sub", "sub2"));

    @Test
    public void test() {
        Assert.isTrue(root.canAccess("any/dir"), "should be allowed to access");
        Assert.isTrue(!sub.canAccess("sub2"), "should not be allowed to access");
        Assert.isTrue(sub.canAccess("sub/any/file"), "should be allowed to access");
        Assert.isTrue(multi.canAccess("sub2"), "should be allowed to access");
        Assert.isTrue(!multi.canAccess("sub3"), "should not be allowed to access");
        Assert.isTrue(multi.canAccess("sub2/any/file"), "should allowed to access");
    }

    @Test
    public void testToString() {
        Assert.isTrue(Objects.equals(new UserContext(List.of("a", "b")).toString(), new UserContext(List.of("b", "a")).toString()), "should be same");
        var ab = new UserContext("a|b");
        Assert.isTrue(ab.canAccess("a"), "should be allowed to access");
        Assert.isTrue(ab.canAccess("a/any"), "should be allowed to access");
        Assert.isTrue(ab.canAccess("b"), "should be allowed to access");
        Assert.isTrue(ab.canAccess("b/any"), "should be allowed to access");
        Assert.isTrue(!ab.canAccess("c"), "should not be allowed to access");
    }
}
