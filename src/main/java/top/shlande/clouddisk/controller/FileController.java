package top.shlande.clouddisk.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.shlande.clouddisk.storage.LocalStorageService;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class FileController {
    private LocalStorageService storageService;

    public FileController(@Autowired LocalStorageService storageService) {
        this.storageService = storageService;
    }

    // 如果不是local，则返回错误
    @PutMapping("/{key}")
    public void upload(@PathVariable String key, @RequestParam int partNumber, @RequestParam String uploadId) {

    }

    // 如果当前是Local类型，则直接写入内容
    // 否则重定向
    @GetMapping("/{key}")
    public Mono<Void> get(@PathVariable String key, ServerHttpResponse response) throws IOException {
        response.setStatusCode(HttpStatus.OK);
        return response.writeWith(
                DataBufferUtils.readInputStream(
                        () -> this.storageService.getObject(key), response.bufferFactory(), 4096)
        );
    }
}
