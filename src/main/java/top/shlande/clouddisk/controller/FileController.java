package top.shlande.clouddisk.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shlande.clouddisk.archive.ArchiveService;
import top.shlande.clouddisk.storage.LocalStorageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("api/file")
public class FileController {
    private final LocalStorageService storageService;
    private final ArchiveService archiveService;

    public FileController(@Autowired LocalStorageService storageService, ArchiveService archiveService) {
        this.storageService = storageService;
        this.archiveService = archiveService;
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
    @GetMapping("/object/{etag}")
    public void get(@PathVariable String etag, @RequestParam(required = false) String filename, HttpServletResponse response) throws IOException {
        if (filename != null) {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        }
        response.setHeader("Etag", etag);
        storageService.getObject(etag).transferTo(response.getOutputStream());
    }

    // 这个方法的存在导致了 vfsService 和 storageService 强耦合，但是现在也没有什么单独部署的需求，先这么做了吧
    // 而且直接在 controller 进行的耦合，不便于测试
    @GetMapping("/archive")
    public void archive(@RequestParam(name = "path") List<String> paths, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("打包下载.zip", StandardCharsets.UTF_8));
        if (paths.size() == 0) {
            paths = List.of("");
        }
        this.archiveService.archive(paths,response.getOutputStream());
    }
}
