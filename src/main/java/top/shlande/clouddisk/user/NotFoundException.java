package top.shlande.clouddisk.user;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String id) {
        super("entity not found:" + id);
    }
}
