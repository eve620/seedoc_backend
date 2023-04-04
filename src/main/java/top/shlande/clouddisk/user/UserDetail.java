package top.shlande.clouddisk.user;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserDetail {
    public String id;
    public String group;
    public UserRole role;
    public String name;
    // 用户能够访问的路径信息
    public List<UserContext> context;

    private UserDetail(String id, String group, UserRole role, String name, List<UserContext> context) {
        this.id = id;
        this.group = group;
        this.role = role;
        this.name = name;
        this.context = context;
    }


    public UserDetail createUser(String name, UserGroup group, UserRole role) {
        onlyAdminCanCreateUser();
        return new UserDetail(
                UUID.randomUUID().toString(), defaultCreateSameGroupUser(group.id), defaultCreateNormalUser(role),
                name, null
        );
    }

    private void onlyAdminCanCreateUser() {
        if (this.role != UserRole.Admin) {
            throw new DenyException(this.id, DenyException.createUserAction);
        }
    }

    private String defaultCreateSameGroupUser(String group) {
        // 如果当前用户为全局管理员，则可以将用户安排给任何组
        if (isGlobalAdmin()) {
            return group;
        }
        // 如果当前用户非全局管理员，则新创建的用户组默认属于当前组
        if (group == null) {
            return this.group;
        }
        if (group.equals(this.group)) {
            return group;
        }
        // 其他情况都拒绝
        throw new DenyException(this.id, DenyException.createUserAction);
    }

    private UserRole defaultCreateNormalUser(UserRole role) {
        return this.role == null ? UserRole.User : role;
    }

    private boolean isGlobalAdmin() {
        return this.group.equals(UserGroup.GlobalGroup) && this.role == UserRole.Admin;
    }

    public boolean canDelete(UserDetail userDetail) {
        if (userDetail == null) {
            return false;
        }
        if (isGlobalAdmin()) {
            return true;
        }
        if (isAdmin() && !userDetail.isAdmin() && Objects.equals(userDetail.group, this.group)) {
            return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return this.role == UserRole.Admin;
    }
}
