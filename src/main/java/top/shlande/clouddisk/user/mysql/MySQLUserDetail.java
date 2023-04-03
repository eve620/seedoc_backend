package top.shlande.clouddisk.user.mysql;

import top.shlande.clouddisk.user.UserContext;
import top.shlande.clouddisk.user.UserRole;

public class MySQLUserDetail {
    public String id;
    public String group;
    public UserRole role;
    public String name;
    public String avatar;
    public UserContext context;
}
