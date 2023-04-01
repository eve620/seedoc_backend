package top.shlande.clouddisk.storage;

public class NilObjectException extends RuntimeException {
    public NilObjectException(String etag) {
        super("unable to open object:" + etag);
    }
}
