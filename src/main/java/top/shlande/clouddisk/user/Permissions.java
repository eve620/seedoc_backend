package top.shlande.clouddisk.user;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;

public class Permissions {
    public final static Permission readAll = new ReadPermission("");
    public final static Permission writeAll = new ReadPermission("");

    public final static String userRoleName = "user";
    public final static String adminRoleName = "admin";

    public final static SimpleRole admin = new SimpleRole(adminRoleName);
    public final static SimpleRole user = new SimpleRole(userRoleName);
}
