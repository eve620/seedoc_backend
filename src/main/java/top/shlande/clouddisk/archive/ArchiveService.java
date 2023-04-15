package top.shlande.clouddisk.archive;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.vfs.VFSService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ArchiveService {
    private VFSService vfsService;
    private LocalStorageService localStorageService;

    public ArchiveService(VFSService vfsService, LocalStorageService localStorageService) {
        this.vfsService = vfsService;
        this.localStorageService = localStorageService;
    }

    public void archive(List<String> paths, OutputStream outputStream) throws IOException {
        // TODO: 这样设计可能会导致同时打开的文件数量过大
        var files = new HashMap<String, Optional<InputStream>>();
        for (var path : paths) {
            for (var entry : vfsService.walk(path).entrySet()) {
                var filePath = entry.getKey().substring(path.length() == 0 ? 0 :path.length() + 1);
                if (entry.getValue().etag == null) {
                    files.put(filePath, Optional.empty());
                    continue;
                }
                files.put(filePath, Optional.of(localStorageService.getObject(entry.getValue().etag)));
            }
        }
        ArchiveUtils.tar(files,outputStream);
    }
}
