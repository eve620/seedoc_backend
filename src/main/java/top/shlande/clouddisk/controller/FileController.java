package top.shlande.clouddisk.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.storage.LocalStorageService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("file")
public class FileController {
    private final LocalStorageService storageService;

    public FileController(@Autowired LocalStorageService storageService) {
        this.storageService = storageService;
    }

    // 如果不是local，则返回错误
    @PutMapping("/{uploadId}")
    public void upload(@PathVariable String uploadId, @RequestParam Integer partNumber, ServletRequest request) throws IOException {
        storageService.putPart(request.getInputStream(), uploadId, partNumber);
    }

    @PutMapping("/{uploadId}?complete")
    public void complete(@PathVariable String uploadId, HttpServletResponse response) {
        try {
            storageService.completeUpload(uploadId);
        } catch (NoSuchAlgorithmException exception) {
            response.setStatus(400);
        } catch (IOException exception) {
            response.setStatus(500);
        }
    }

    // 如果当前是Local类型，则直接写入内容
    // 否则重定向
    // For more injectable argument, please refer to spring doc:
    //  https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
    @GetMapping("/{etag}")
    public void get(@PathVariable String etag, HttpServletResponse response) throws IOException {
        storageService.getObject(etag).transferTo(response.getOutputStream());
    }

}
