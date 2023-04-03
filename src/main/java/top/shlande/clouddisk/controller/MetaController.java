package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.vfs.FileInfo;
import top.shlande.clouddisk.vfs.VFSService;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequestMapping("meta")
@RestController
public class MetaController {
    private final LocalStorageService storageService;
    private final VFSService vfsService;

    public MetaController(@Autowired LocalStorageService storageService, @Autowired VFSService vfsService) {
        this.storageService = storageService;
        this.vfsService = vfsService;
    }

    @GetMapping
    public List<FileInfo> list(@RequestParam String prefix,
                               @RequestParam(required = false, defaultValue = "50") int maxKeys,
                               @RequestParam(required = false, defaultValue = "0") int startAfter) {
        return vfsService.list(prefix, maxKeys, startAfter);
    }

    @GetMapping("/{key}")
    public void download(@PathVariable String key, HttpServletResponse response) {
        var fileInfo = vfsService.get(key);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "/file/" + fileInfo.etag);
    }

    @PutMapping("/{key}")
    public String createUpload(@PathVariable String key, HttpServletRequest request) throws Exception {
        // TODO: add owner service
        var owner = "testOwner";
        var filePath = Path.of(key);
        var fileInfo = new FileInfo();
        fileInfo.contentType = request.getHeader("Content-Type");
        fileInfo.name = filePath.getFileName().toString();
        fileInfo.owner = owner;
        fileInfo.uploadId = storageService.createUpload();
        var parent = filePath.getParent() == null ? "" : filePath.getParent().toString();
        vfsService.create(parent, fileInfo);
        return fileInfo.uploadId;
    }

    // TODO：如何完成 ? 匹配？
    // @PutMapping("/{uploadId}?complete")
    @PutMapping("/{uploadId}/complete")
    public String complete(@PathVariable String uploadId, HttpServletResponse response) {
        String etag = null;
        try {
            etag = storageService.completeUpload(uploadId);
        } catch (IOException exception) {
            response.setStatus(500);
        }
        if (etag != null) {
            vfsService.complete(uploadId, etag);
        }
        return etag;
    }

}
