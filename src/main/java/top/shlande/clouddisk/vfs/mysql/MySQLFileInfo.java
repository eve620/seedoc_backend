package top.shlande.clouddisk.vfs.mysql;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import top.shlande.clouddisk.vfs.FileInfo;

import java.util.Date;

@NoArgsConstructor
@Table(name = "files")
public class MySQLFileInfo {
    @Id
    public Long id;
    public String name;
    public Integer size;
    public String contentType;
    public String owner;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date lastModified;
    public String etag;
    public String uploadId;
    // for index only
    public String parent;

    public MySQLFileInfo(FileInfo info, String parent) {
        this.name = info.name;
        this.size = info.size;
        this.contentType = info.contentType;
        this.owner = info.owner;
        this.created = info.created;
        this.lastModified = info.lastModified;
        this.etag = info.etag;
        this.uploadId = info.uploadId;
        this.parent = parent;
    }

    public FileInfo toFileInfo() {
        var result = new FileInfo();
        result.name = this.name;
        result.size = this.size;
        result.contentType = this.contentType;
        result.owner = this.owner;
        result.created = this.created;
        result.lastModified = this.lastModified;
        result.etag = this.etag;
        result.uploadId = this.uploadId;
        return result;
    }
}
