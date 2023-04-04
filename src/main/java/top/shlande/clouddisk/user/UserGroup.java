package top.shlande.clouddisk.user;

import java.util.UUID;

public class UserGroup {
    public static final String GlobalGroup = "";
    public String id;
    public String name;
    // 用户组权限
    public UserContext context;

    public UserGroup(UserDetail operator, String name, UserContext context) {
        if (!operator.isGlobalAdmin()) {
            throw new DenyException(operator.id, "");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.context = context;
    }

    public UserGroup(String id, String name, UserContext context) {
        this.id = id;
        this.name = name;
        this.context = context;
    }

    public void edit(UserDetail operator, String name, UserContext context) {
        if (!operator.isGlobalAdmin()) {
            throw new DenyException(operator.id, "");
        }
        this.setWithDefault(name, context);
    }

    public void delete(UserDetail operator) {
        if (!operator.isGlobalAdmin()) {
            throw new DenyException(operator.id, "");
        }
    }

    private void setWithDefault(String name, UserContext context) {
        if (name != null) {
            this.name = name;
        }
        if (context != null) {
            this.context = context;
        }
    }
}
