package top.shlande.clouddisk.vfs;

public class NilDirException extends RuntimeException {
    public NilDirException(String dir) {
        super("dir doesn't exist:" + dir);
    }
}
