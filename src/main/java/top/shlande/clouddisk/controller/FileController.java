package top.shlande.clouddisk.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.vfs.FileInfo;
import top.shlande.clouddisk.vfs.VFSService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/file")
public class FileController {
    private final LocalStorageService storageService;

    public FileController(@Autowired LocalStorageService storageService) {
        this.storageService = storageService;
    }

    // 如果不是local，则返回错误
    @PutMapping("/{uploadId}")
    public void upload(@PathVariable String uploadId, @RequestParam Integer part, ServletRequest request) throws IOException {
        storageService.putPart(request.getInputStream(), uploadId, part);
    }

    // 如果当前是Local类型，则直接写入内容
    // 否则重定向
    // For more injectable argument, please refer to spring doc:
    //  https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
    @GetMapping("/{etag}")
    public void get(@PathVariable String etag, @RequestParam(required = false) String filename, HttpServletResponse response) throws IOException {
        if (filename != null) {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        }
        response.setHeader("Etag", etag);
        storageService.getObject(etag).transferTo(response.getOutputStream());
    }

}
