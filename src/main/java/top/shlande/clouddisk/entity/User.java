package top.shlande.clouddisk.entity;

import top.shlande.clouddisk.user.BadRequestException;
import top.shlande.clouddisk.user.DenyException;

import java.util.UUID;

public class User {
    public enum Role {
        ADMIN,
        USER,
    }

    public String id;
    public String name;
    public String password;
    public Role role;
    public UserContext context;

    public User(String id, String name, String password, Role role, UserContext permission) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.context = permission;
    }

    public User createUser(String id, String name, String password, Role role, UserContext permission) {
        onlyAdminCanCreateUser();
        if (!isContextAdmin(permission)) {
            throw new DenyException(this.id, "createUser");
        }
        return new User(
                id, name, password, defaultCreateNormalUser(role), permission
        );
    }

    public User createUser(String name, String password, Role role, UserContext permission) {
        return createUser(UUID.randomUUID().toString(), name, password, role, permission);
    }

    public boolean canWrite(String dir) {
        return this.context.canAccess(dir);
    }

    // 当前用户更新其他用户信息
    public void update(User user, String name, String password, Role role, UserContext permission) {
        permission = defaultCreateSamePermissionUser(permission);
        if (this.isContextAdmin(permission)) {
            user.setWithDefault(name, password, role, permission);
            return;
        }
        throw new DenyException(this.id, DenyException.updateUserAction);
    }

    // 判断用户是否可以更改其他账户的登录信息
    public boolean canSetCredential(User user) {
        return this.isContextAdmin(user.context) || this.id.equals(user.id);
    }

    private void setWithDefault(String name, String password, Role role, UserContext context) {
        if (name != null) {
            this.name = name;
        }
        if (role != null) {
            this.role = role;
        }
        if (context != null) {
            this.context = context;
        }
        if (password != null) {
            setPassword(password);
        }
    }

    private void onlyAdminCanCreateUser() {
        if (!isAdmin()) {
            throw new DenyException(this.id, DenyException.createUserAction);
        }
    }

    private UserContext defaultCreateSamePermissionUser(UserContext permission) {
        if (permission == null) {
            permission = this.context;
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
        return user != null && (isGlobalAdmin() || isContextAdmin(user.context) && !user.isAdmin());
    }

    private Role defaultCreateNormalUser(Role role) {
        return role == null ? Role.USER : role;
    }

    public boolean isGlobalAdmin() {
        return isContextAdmin(UserContext.global);
    }

    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN);
    }

    // 如果 groupId 为空，返回false
    private boolean isContextAdmin(UserContext context) {
        return this.isAdmin() && this.context.canAccess(context.toString());
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new BadRequestException(this.id, "password too weak");
        }
        this.password = password;
    }
}
