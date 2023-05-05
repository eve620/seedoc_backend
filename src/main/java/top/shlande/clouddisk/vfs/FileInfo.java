package top.shlande.clouddisk.vfs;

import top.shlande.clouddisk.PathUtils;

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
        fileInfo.name = PathUtils.filename(key);
        fileInfo.contentType = "dir";
        fileInfo.lastModified = now;
        fileInfo.created = now;
        return fileInfo;
    }
    public static FileInfo file(String key, String contentType,String uploadId, String owner) {
        var fileInfo = new FileInfo();
        fileInfo.name = PathUtils.filename(key);
        fileInfo.contentType = contentType;
        fileInfo.owner = owner;
        fileInfo.lastModified = new Date();
        fileInfo.uploadId = uploadId;
        return fileInfo;
    }
}
