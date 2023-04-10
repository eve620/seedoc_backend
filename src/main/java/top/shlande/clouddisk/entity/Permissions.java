package top.shlande.clouddisk.entity;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;

public class Permissions {
    public final static Permission global = new WildcardPermission("*");

    public final static String userRoleName = "USER";
    public final static String adminRoleName = "ADMIN";

    public final static SimpleRole admin = new SimpleRole(adminRoleName);
    public final static SimpleRole user = new SimpleRole(userRoleName);
}
