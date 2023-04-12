package top.shlande.clouddisk.vfs.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import top.shlande.clouddisk.vfs.FileInfo;
import top.shlande.clouddisk.vfs.NilDirException;
import top.shlande.clouddisk.vfs.NotEmptyException;
import top.shlande.clouddisk.vfs.VFSService;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MySQLVFSService implements VFSService {
    private final MySQLFileInfoRepository repository;

    public MySQLVFSService(@Autowired MySQLFileInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(String dirKey, FileInfo info) {
        //检查parent文件夹是否存在
        boolean hasParent;
        if (dirKey.length() == 0) {
            hasParent = true;
        } else {
            var parentPath = Path.of(dirKey);
            var ppDir = parentPath.getParent();
            hasParent = this.repository.getByPath(
                    ppDir == null ? "" : ppDir.toString(), parentPath.getFileName().toString()
            ) != null;
        }
        if (!hasParent) {
            throw new NilDirException(dirKey);
        }
        // 检查是否已经重复出现，如果重复出现，就覆盖
        var duplicated = this.repository.getByPath(dirKey, info.name);
        var newInfo = new MySQLFileInfo(info, dirKey);
        if (duplicated != null) {
            newInfo.id = duplicated.id;
        }
        this.repository.save(newInfo);
    }

    @Override
    public List<FileInfo> list(String dirKey, Integer maxKey, Integer start) {
        var infos = this.repository.list(dirKey, maxKey, start);
        var result = new ArrayList<FileInfo>(infos.size());
        for (MySQLFileInfo info : infos) {
            result.add(info.toFileInfo());
        }
        return result;
    }

    @Override
    public List<FileInfo> walk(String dirKey) {
        var infos = walkDir(dirKey);
        var result = new ArrayList<FileInfo>(infos.size());
        for (MySQLFileInfo info : infos) {
            result.add(info.toFileInfo());
        }
        return result;
    }

    @Override
    public void rename(String src, String dst) {
        var parentSrc = getParent(src);
        var nameSrc = getFilename(src);
        // 查看src文件是否存在
        var srcFile = this.repository.getByPath(parentSrc,nameSrc);
        if (srcFile == null) {
            throw new NilDirException(src);
        }
        // 查看dst文件是否存在
        var parentDst = getParent(dst);
        var nameDst = getFilename(dst);
        // 查看src文件是否存在
        var dstFile = this.repository.getByPath(parentDst,nameDst);
        if (dstFile != null) {
            throw new NotEmptyException(dst);
        }
        this.repository.moveDir(src,dst);
        this.repository.moveFile(parentDst,nameDst,srcFile.id);
    }

    private List<MySQLFileInfo> walkDir(String dirKey) {
        var parentLike = dirKey + "/%";
        if (dirKey.length() == 0) {
            parentLike = "%";
        }
        return this.repository.walk(dirKey, parentLike);
    }

    public void complete(String uploadId, String etag, Long size) {
        this.repository.complete(uploadId, etag, size);
    }

    @Override
    public FileInfo get(String path) {
        var fileInfo = this.repository.getByPath(getParent(path), getFilename(path));
        return fileInfo == null ? null : fileInfo.toFileInfo();
    }

    @Override
    public FileInfo getByUploadId(String uploadId) {
        var file = this.repository.getByUploadId(uploadId);
        if (file == null) {
            return null;
        }
        return file.toFileInfo();
    }

    @Override
    public void delete(String key) {
        // 如果是文件夹，则删除所有子文件
        deleteAll(walkDir(key));
        // 删除文件本身
        this.repository.deleteByPath(getParent(key), getFilename(key));
    }

    private void deleteAll(List<MySQLFileInfo> infos) {
        if (infos == null || infos.size() == 0) {
            return;
        }
        var ids = new ArrayList<Long>(infos.size());
        for (MySQLFileInfo sub : infos) {
            ids.add(sub.id);
        }
        this.repository.deleteAllById(ids);
    }

    private String getParent(String path) {
        var ppath = Path.of(path).getParent();
        return ppath == null ? "" : ppath.toString();
    }


    private String getFilename(String path) {
        return Path.of(path).getFileName().toString();
    }
}
