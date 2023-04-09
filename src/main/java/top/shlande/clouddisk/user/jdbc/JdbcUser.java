package top.shlande.clouddisk.user.jdbc;

import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;
import top.shlande.clouddisk.entity.Permissions;
import top.shlande.clouddisk.entity.User;


public class JdbcUser {
    public enum Role {
        ADMIN,
        USER,
    }

    public String id;
    public String name;
    public String password;
    public Role role;
    public String permission;

    public JdbcUser(User detail) {
        this.id = detail.id;
        this.name = detail.name;
        this.password = detail.password;
        switch (detail.role.getName()) {
            case Permissions.userRoleName -> {
                this.role = Role.USER;
            }
            case Permissions.adminRoleName -> {
                this.role = Role.ADMIN;
            }
        }
        this.permission = detail.permission.toString();
    }

    public User toUserDetail() {
        SimpleRole role = null;
        switch (this.role) {
            case USER -> {
                role = Permissions.user;
            }
            case ADMIN -> {
                role = Permissions.admin;
            }
        }
        return new User(
                this.id, this.name, this.password, role, new WildcardPermission(this.permission)
        );
    }
}
