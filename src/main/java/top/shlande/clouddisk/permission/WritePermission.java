package top.shlande.clouddisk.permission;

import org.apache.shiro.authz.permission.WildcardPermission;

public class WritePermission extends WildcardPermission {
    public static final String writePermission = "write";

    // 赋予当前目录及其子目录的修改权限
    // 如果是文件夹，结尾必须自己手动加 /
    public WritePermission(String dir) {
        super(writePermission + ":" + dir + "*");
    }
}
