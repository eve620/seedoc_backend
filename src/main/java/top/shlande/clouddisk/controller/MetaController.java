package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.entity.User;
import top.shlande.clouddisk.storage.CompleteUploadResult;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.user.DenyException;
import top.shlande.clouddisk.user.UserService;
import top.shlande.clouddisk.vfs.FileInfo;
import top.shlande.clouddisk.vfs.VFSService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RequestMapping("meta")
@RestController
public class MetaController {
    private final LocalStorageService storageService;
    private final VFSService vfsService;
    private final UserService userService;

    public MetaController(@Autowired UserService userService, @Autowired LocalStorageService storageService, @Autowired VFSService vfsService) {
        this.storageService = storageService;
        this.userService = userService;
        this.vfsService = vfsService;
    }

    // TODO: @GetMapping("/{prefix}?list")
    @GetMapping
    public List<FileInfo> list(@RequestParam String prefix,
                               @RequestParam(required = false, defaultValue = "50") int maxKeys,
                               @RequestParam(required = false, defaultValue = "0") int startAfter) {
        return vfsService.list(prefix, maxKeys, startAfter);
    }

    @GetMapping("/{*key}")
    public void download(@PathVariable String key, HttpServletResponse response) {
        key = deleteSlashPrefix(key);
        var fileInfo = vfsService.get(key);
        if (fileInfo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "/file/" + fileInfo.etag);
    }

    @PutMapping("/{*key}")
    public String createUpload(@PathVariable String key, HttpServletRequest request) throws Exception {
        key = deleteSlashPrefix(key);
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
        CompleteUploadResult result = null;
        try {
            result = storageService.completeUpload(uploadId);
        } catch (IOException exception) {
            response.setStatus(500);
            return "";
        }
        if (result != null) {
            vfsService.complete(uploadId, result.etag, result.size);
        }
        return result.etag;
    }

    @DeleteMapping("/{*key}")
    public void delete(@PathVariable("key") String key, HttpServletRequest http, HttpServletResponse response) {
        key = deleteSlashPrefix(key);
        var user = getUser();
        // 检查用户是否有资格
        if (!user.canWrite(key)) {
            response.setStatus(400);
            return;
        }
        this.vfsService.delete(key);
    }

    // TODO: 重新调整一下地址
    @PostMapping("/{*key}")
    public void createDir(@PathVariable String key, HttpServletRequest request) {
        key = deleteSlashPrefix(key);
        var parent = Path.of(key).getParent();
        var parentKey = parent == null ? "" : parent.toString();
        var user = getUser();
        if (!user.canWrite(parentKey)) {
            throw new DenyException(user.id, "createDir");
        }
        var dir = FileInfo.dir(key);
        dir.owner = user.id;
        this.vfsService.create(parentKey, dir);
    }

    @GetMapping("/user/{id}")
    private User getUser(@PathVariable("id") String id) {
        return this.userService.user(id);
    }

    @GetMapping("/user/whoami")
    private User whoAmI(HttpServletRequest http) {
        return this.userService.user(getUserId());
    }

    private String getUserId() {
        var subject = SecurityUtils.getSubject();
        if (subject == null) {
            return null;
        }
        return (String) subject.getPrincipal();
    }

    private User getUser() {
        return this.userService.user(this.getUserId());
    }


    private String deleteSlashPrefix(String key) {
        if (key != null && key.indexOf("/") == 0) {
            return key.substring(1);
        }
        return key;
    }
}
