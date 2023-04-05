package top.shlande.clouddisk.user;

public enum UserRole {
    // 管理员，能够管理当前分组中的所有用户
    ADMIN,
    // 普通用户，只能写入所属组的文件
    USER
}
