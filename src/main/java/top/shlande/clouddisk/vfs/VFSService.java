package top.shlande.clouddisk.vfs;

import java.util.List;
import java.util.Map;

// TODO: rename 和其他 walk 存在底层实习问题，可能需要一层抽象
public interface VFSService {
    public void create(String dirKey, FileInfo info);

    public List<FileInfo> list(String dirKey, Integer maxKey, Integer start);

    public Map<String,FileInfo>  walk(String dirKey);

    public void rename(String src,String dst);

    // 通过路径获取文件信息
    public FileInfo get(String path);

    public FileInfo getByUploadId(String uploadId);

    public void complete(String uploadId, String etag, Long size);

    // 删除文件，如果是文件夹则递归删除
    public void delete(String key);
}
