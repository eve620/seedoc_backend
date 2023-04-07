package top.shlande.clouddisk.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;

public class Permissions {
    public final static Permission readAll = new ReadPermission("");
    public final static Permission writeAll = new ReadPermission("");

    public final static SimpleRole admin = new SimpleRole("admin");
    public final static SimpleRole user = new SimpleRole("user");
}
