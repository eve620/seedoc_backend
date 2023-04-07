package top.shlande.clouddisk.user;

import org.apache.shiro.authz.permission.WildcardPermission;

public class ReadPermission extends WildcardPermission {
    public static final String readPermission = "read";
    // 赋予当前目录及其子目录的读取
    // 如果是文件夹，结尾必须自己手动加 /
    public ReadPermission(String dir) {
        super(readPermission + ":" + dir + "*");
    }
}
