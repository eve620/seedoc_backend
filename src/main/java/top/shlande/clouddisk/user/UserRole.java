package top.shlande.clouddisk.user;

public class UserRole {
    public enum Role {
        // 管理员，能够管理当前分组中的所有用户
        Admin,
        // 普通用户，只能写入所属组的文件
        User
    }

    // 用户本身的权限
    public UserContext context;
    // 用户角色
    public Role role;
}
