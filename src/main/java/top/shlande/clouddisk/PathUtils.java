package top.shlande.clouddisk;


import org.apache.tomcat.util.buf.StringUtils;

import java.util.Arrays;
import java.util.List;

public class PathUtils {
    // 获取文件夹
    public static String directory(String path) {
        if (path == null || path.length() == 0) {
            return "";
        }
        if (path.equals("/")) {
            return "/";
        }
        var raw = path.split("/");
        if (raw.length < 1) {
            return "";
        }
        return StringUtils.join(List.of(Arrays.copyOfRange(raw, 0, raw.length - 1)),'/');
    }
    // 获取文件名
    public static String filename(String path) {
        if (path == null || path.length() == 0) {
            return "";
        }
        var raw = path.split("/");
        return raw[raw.length-1];
    }
}
