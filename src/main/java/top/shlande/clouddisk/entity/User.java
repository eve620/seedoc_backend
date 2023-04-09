package top.shlande.clouddisk.entity;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;
import top.shlande.clouddisk.user.DenyException;

import java.util.UUID;

public class User {
    public String id;
    public String name;
    public String password;
    public SimpleRole role;
    public Permission permission;

    public User(String id, String name, String password, SimpleRole role, Permission permission) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.permission = permission;
    }


    public User createUser(String name, String password, SimpleRole role, Permission permission) {
        onlyAdminCanCreateUser();
        return new User(
                UUID.randomUUID().toString(), name, password, defaultCreateNormalUser(role), null
        );
    }

    public boolean canWrite(String dir) {
        return this.permission.implies(new WildcardPermission(dir));
    }

    // 当前用户更新其他用户信息
    public void update(User user, String name, SimpleRole role, Permission permission) {
        permission = defaultCreateSamePermissionUser(permission);
        if (this.isContextAdmin(permission)) {
            user.setWithDefault(name, role, permission);
        }
        throw new DenyException(this.id, DenyException.updateUserAction);
    }

    // 判断用户是否可以更改其他账户的登录信息
    public boolean canSetCredential(User user) {
        return this.isContextAdmin(user.permission) || this.id.equals(user.id);
    }

    private void setWithDefault(String name, SimpleRole role, Permission context) {
        if (name != null) {
            this.name = name;
        }
        if (role != null) {
            this.role = role;
        }
        if (context != null) {
            this.permission = context;
        }
    }

    private void onlyAdminCanCreateUser() {
        if (!isAdmin()) {
            throw new DenyException(this.id, DenyException.createUserAction);
        }
    }

    private Permission defaultCreateSamePermissionUser(Permission permission) {
        if (permission == null) {
            permission = this.permission;
        }
        // 如果当前用户为全局管理员，则可以将用户安排给任何组
        if (isGlobalAdmin()) {
            return permission;
        }
        // 如果当前用户非全局管理员，则新创建的用户组默认属于当前组
        if (permission != null && isContextAdmin(permission)) {
            return permission;
        }
        // 其他情况都拒绝
        throw new DenyException(this.id, DenyException.createUserAction);
    }

    public boolean canDelete(User user) {
        return user != null && isContextAdmin(user.permission);
    }

    private SimpleRole defaultCreateNormalUser(SimpleRole role) {
        return role == null ? Permissions.user : role;
    }

    public boolean isGlobalAdmin() {
        return isContextAdmin(Permissions.global);
    }

    public boolean isAdmin() {
        return this.role.equals(Permissions.admin);
    }

    // 如果 groupId 为空，返回false
    private boolean isContextAdmin(Permission context) {
        return this.isAdmin() && this.permission.implies(context);
    }

}
