package top.shlande.clouddisk.vfs;

import java.util.List;

public interface VFSService {
    public void create(String dirKey, FileInfo info);

    public List<FileInfo> list(String dirKey, Integer maxKey, Integer start);

    public List<FileInfo> walk(String dirKey);

    // 通过路径获取文件信息
    public FileInfo get(String path);

    public FileInfo getByUploadId(String uploadId);

    public void complete(String uploadId, String etag);

    // 删除文件，如果是文件夹则递归删除
    public void delete(String key);
}
