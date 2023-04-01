package top.shlande.clouddisk.vfs;

import java.util.List;

public interface VFSService {
    public void create(String dirKey, FileInfo info);

    public List<FileInfo> list(String dirKey, Integer maxKey, Integer start);

    public FileInfo get(String key);
}
