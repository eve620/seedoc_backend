package top.shlande.clouddisk.vfs;

import java.util.List;

public class MemoryVFSService implements VFSService {
    @Override
    public void create(String dirKey, FileInfo info) {}

    @Override
    public List<FileInfo> list(String dirKey, Integer maxKey, Integer start) {
        return null;
    }

    @Override
    public List<FileInfo> walk(String dirKey) {
        return null;
    }

    @Override
    public FileInfo get(String key) {
        return null;
    }

    @Override
    public void delete(String key) {

    }
}
