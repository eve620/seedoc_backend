package top.shlande.clouddisk.vfs;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException(String dst) {
        super("文件夹不为空:" + dst);
    }
}
