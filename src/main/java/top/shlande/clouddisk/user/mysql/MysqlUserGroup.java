package top.shlande.clouddisk.user.mysql;

import top.shlande.clouddisk.user.UserContext;
import top.shlande.clouddisk.user.UserGroup;

public class MysqlUserGroup {
    public String id;
    public String name;
    public String context;

    public MysqlUserGroup(UserGroup group) {
        this.id = group.id;
        this.name = group.name;
        this.context = group.context.toString();
    }

    public UserGroup toGroup() {
        return new UserGroup(this.id, this.name, new UserContext(this.context));
    }
}
