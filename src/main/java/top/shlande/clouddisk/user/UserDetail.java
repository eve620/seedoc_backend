package top.shlande.clouddisk.user;

import java.util.Objects;
import java.util.UUID;

public class UserDetail {
    public String id;
    public String group;
    public UserRole role;
    public String name;
    // 用户能够访问的路径信息
    public UserContext context;

    public UserDetail(String id, String group, UserRole role, String name, UserContext context) {
        this.id = id;
        this.group = group;
        this.role = role;
        this.name = name;
        this.context = context;
    }


    public UserDetail createUser(String name, UserGroup group, UserRole role) {
        var groupId = defaultCreateSameGroupUser(group == null ? null : group.id);
        onlyAdminCanCreateUser();
        return new UserDetail(
                UUID.randomUUID().toString(), groupId, defaultCreateNormalUser(role),
                name, null
        );
    }

    // 当前用户更新其他用户信息
    public void update(UserDetail user, String name, UserGroup group, UserRole role, UserContext context) {
        if (this.isGlobalAdmin() || this.isGroupAdmin(group.id)) {
            user.setWithDefault(name, group, role, context);
        }
        throw new DenyException(this.id, DenyException.updateUserAction);
    }

    // 判断用户是否可以更改其他账户的登录信息
    public void setCredential(UserDetail user) {
        if (this.isGlobalAdmin() || (this.isGroupAdmin(user.group) && !user.isAdmin())) {
            return;
        }
        throw new DenyException(this.id, "");
    }

    private void setWithDefault(String name, UserGroup group, UserRole role, UserContext context) {
        if (name != null) {
            this.name = name;
        }
        if (group != null) {
            this.group = group.id;
        }
        if (role != null) {
            this.role = role;
        }
        if (context != null) {
            this.context = context;
        }
    }

    private void onlyAdminCanCreateUser() {
        if (!isAdmin()) {
            throw new DenyException(this.id, DenyException.createUserAction);
        }
    }

    private String defaultCreateSameGroupUser(String groupId) {
        // 如果当前用户为全局管理员，则可以将用户安排给任何组
        if (isGlobalAdmin()) {
            return groupId;
        }
        // 如果当前用户非全局管理员，则新创建的用户组默认属于当前组
        if ((groupId != null && isGroupAdmin(groupId)) || isAdmin()) {
            return groupId == null ? this.group : groupId;
        }
        // 其他情况都拒绝
        throw new DenyException(this.id, DenyException.createUserAction);
    }

    public boolean canDelete(UserDetail userDetail) {
        return userDetail != null && (isGlobalAdmin() || isGroupAdmin(userDetail.group) && !userDetail.isAdmin());
    }

    private UserRole defaultCreateNormalUser(UserRole role) {
        return role == null ? UserRole.USER : role;
    }

    public boolean isGlobalAdmin() {
        return this.group.equals(UserGroup.GlobalGroup) && this.role == UserRole.ADMIN;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    // 如果 groupId 为空，返回false
    private boolean isGroupAdmin(String groupId) {
        return isAdmin() && Objects.equals(this.group, groupId);
    }

}
