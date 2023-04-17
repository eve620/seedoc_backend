package top.shlande.clouddisk.vfs;

import java.nio.file.Path;
import java.util.Date;

public class FileInfo {
    public String name;
    public Integer size;
    public String contentType;
    public String owner;
    public Date created;
    public Date lastModified;
    public String etag;
    public String uploadId;
    public static FileInfo dir(String key) {
        var now = new Date();
        var fileInfo = new FileInfo();
        fileInfo.name = Path.of(key).getFileName().toString();
        fileInfo.contentType = "dir";
        fileInfo.lastModified = now;
        fileInfo.created = now;
        return fileInfo;
    }
}
