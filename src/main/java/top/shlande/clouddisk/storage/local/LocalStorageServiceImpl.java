package top.shlande.clouddisk.storage.local;

import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.storage.NilObjectException;
import top.shlande.clouddisk.storage.part.PartService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class LocalStorageServiceImpl implements LocalStorageService {
    private PartService partService;
    private final MessageDigest digest = MessageDigest.getInstance("MD5");
    private final Path tempPath;
    private final Path rootPath;

    private final static String temp = "temp";

    private static final FileAttribute<Set<PosixFilePermission>> permissionSet = PosixFilePermissions.asFileAttribute(
            new HashSet<>(List.of(PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE))
    );

    public LocalStorageServiceImpl(String root) throws IOException, NoSuchAlgorithmException {
        // prepare dir for store
        this.rootPath = Files.createDirectory(Paths.get(root), permissionSet);
        this.tempPath = Files.createDirectory(this.rootPath.resolve(temp), permissionSet);
    }

    // 上传分片
    private String putParts(InputStream stream, String uploadId, Integer part) throws IOException {
        var partsPath = tempPath.resolve(uploadId);
        var partPath = partsPath.resolve(part.toString());
        var output = new DigestOutputStream(Files.newOutputStream(partPath), digest);
        stream.transferTo(output);
        output.close();
        var md5 = output.getMessageDigest().toString();
        Files.move(partPath, partsPath.resolve(md5));
        // 保存信息
        partService.put(uploadId, md5, part);
        return md5;
    }


    @Override
    public String createUpload() throws Exception {
        var uploadId = partService.create();
        // create dir for uploading
        Files.createDirectory(tempPath.resolve(uploadId), permissionSet);
        return uploadId;
    }

    @Override
    public void putPart(InputStream stream, String uploadId, int part) throws IOException {
        var parts = partService.get(uploadId);
        // 打开文件
        var partFile = Files.createFile(tempPath.resolve(Integer.toString(part)), permissionSet);
        // 开始写入
        Files.copy(stream, partFile);
    }

    @Override
    public String completeUpload(String uploadId) throws IOException, NoSuchAlgorithmException {
        var parts_ = partService.get(uploadId);
        var partPath = tempPath.resolve(uploadId);
        // 创建主文件
        var combinedPath = partPath.resolve("combined");
        var combinedStream = new DigestOutputStream(
                Files.newOutputStream(combinedPath), MessageDigest.getInstance("MD5")
        );
        // 将文件进行组合
        for (Map.Entry<Integer, String> entry : parts_.entrySet()) {
            var part = Files.newInputStream(partPath.resolve(entry.getKey().toString()));
            part.transferTo(combinedStream);
            part.close();
        }
        // 关闭主文件并移动
        combinedStream.close();
        var md5 = combinedStream.getMessageDigest();
        Files.move(combinedPath, rootPath.resolve(md5.toString()));
        return md5.toString();
    }

    @Override
    public InputStream getObject(String etag) throws IOException {
        var objectPath = rootPath.resolve(etag);
        if (!objectPath.toFile().exists()) {
            throw new NilObjectException(etag);
        }
        return Files.newInputStream(objectPath);
    }
}
