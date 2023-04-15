package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.entity.User;
import top.shlande.clouddisk.storage.CompleteUploadResult;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.user.DenyException;
import top.shlande.clouddisk.user.NotFoundException;
import top.shlande.clouddisk.user.UserService;
import top.shlande.clouddisk.vfs.FileInfo;
import top.shlande.clouddisk.vfs.VFSService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@RequestMapping("api/meta")
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
                               @RequestParam(required = false, defaultValue = "1000") int maxKeys,
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
        response.setHeader("Location", "/file/" + fileInfo.etag + "?filename=" + URLEncoder.encode(fileInfo.name, StandardCharsets.UTF_8));
    }

    @PutMapping("/{*key}")
    public String createOrDelete(@PathVariable String key, @RequestParam(required = false, defaultValue = "false") Boolean delete, HttpServletRequest request) throws Exception {
        if (delete) {
            delete(key, request);
            return "";
        }
        return create(key, request);
    }

    @GetMapping("/rename")
    public void rename(HttpServletRequest http, @RequestParam("src") String src, @RequestParam("dst") String dst) {
        var user = getUser(http);
        if (!user.canWrite(src)) {
            throw new DenyException(user.id, "write to " + dst);
        }
        this.vfsService.rename(src, dst);
    }

    public String create(String key, HttpServletRequest request) throws Exception {
        // 判断是否能够写入
        key = deleteSlashPrefix(key);
        var user = getUser(request);
        if (!user.canWrite(key)) {
            throw new DenyException(user.id, "write");
        }
        //key = deleteSlashPrefix(key);
        // TODO: add owner service
        var owner = getUserId(request);
        var filePath = Path.of(key);
        var fileInfo = new FileInfo();
        var now = new Date();
        fileInfo.contentType = request.getHeader("Content-Type");
        fileInfo.name = filePath.getFileName().toString();
        fileInfo.owner = owner;
        fileInfo.uploadId = storageService.createUpload();
        var parent = filePath.getParent() == null ? "" : filePath.getParent().toString();
        vfsService.create(parent, fileInfo);
        return fileInfo.uploadId;
    }

    public void delete(String key, HttpServletRequest http) {
        key = deleteSlashPrefix(key);
        var user = getUser(http);
        // 检查用户是否有资格
        if (!user.canWrite(key)) {
            throw new DenyException(user.id, "delete");
        }
        this.vfsService.delete(key);
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

    // TODO: 重新调整一下地址
    @PostMapping("/{*key}")
    public void createDir(@PathVariable String key, HttpServletRequest request) {
        key = deleteSlashPrefix(key);
        var parent = Path.of(key).getParent();
        var parentKey = parent == null ? "" : parent.toString();
        var user = getUser(request);
        if (!user.canWrite(parentKey)) {
            throw new DenyException(user.id, "createDir");
        }
        var dir = FileInfo.dir(key);
        dir.owner = user.id;
        this.vfsService.create(parentKey, dir);
    }

    @GetMapping("/user/{id}")
    private User getUser(@PathVariable("id") HttpServletRequest http) {
        return this.userService.user(getUserId(http));
    }

    @GetMapping("/user/whoami")
    private User whoAmI(HttpServletRequest http) {
        return this.userService.user(getUserId(http));
    }

    private String getUserId(HttpServletRequest http) {
        var session = http.getSession(false);
        if (session == null) {
            throw new DenyException("", "not login");
        }
        return (String) session.getAttribute("userId");
    }


    private String deleteSlashPrefix(String key) {
        if (key != null && key.indexOf("/") == 0) {
            return key.substring(1);
        }
        return key;
    }
}
