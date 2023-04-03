package top.shlande.clouddisk.storage.local;

import org.springframework.beans.factory.annotation.Autowired;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.storage.NilObjectException;
import top.shlande.clouddisk.storage.part.PartService;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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

    private static final FileAttribute<Set<PosixFilePermission>> dirPermission = PosixFilePermissions.asFileAttribute(
            new HashSet<>(List.of(PosixFilePermission.OWNER_READ, PosixFilePermission.GROUP_WRITE, PosixFilePermission.OWNER_EXECUTE,
                    PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_EXECUTE)
            )
    );
    private static final FileAttribute<Set<PosixFilePermission>> filePermission = PosixFilePermissions.asFileAttribute(
            new HashSet<>(List.of(PosixFilePermission.OWNER_READ, PosixFilePermission.GROUP_WRITE))
    );

    public LocalStorageServiceImpl(String root, @Autowired PartService partService) throws IOException, NoSuchAlgorithmException {
        this.partService = partService;
        // prepare dir for store
        rootPath = Paths.get(root);
        if (!rootPath.toFile().exists()) {
            Files.createDirectory(rootPath);
        }
        tempPath = rootPath.resolve(temp);
        if (!tempPath.toFile().exists()) {
            Files.createDirectory(tempPath);
        }
    }

    // 上传分片
    private String putParts(InputStream stream, String uploadId, Integer part) throws IOException {
        var partsPath = tempPath.resolve(uploadId);
        var partPath = partsPath.resolve(part.toString());
        var output = new DigestOutputStream(Files.newOutputStream(partPath), digest);
        stream.transferTo(output);
        output.close();
        var md5 = HexFormat.of().formatHex(output.getMessageDigest().digest());
        Files.move(partPath, partsPath.resolve(md5));
        // 保存信息
        partService.put(uploadId, md5, part);
        return md5;
    }


    @Override
    public String createUpload() throws Exception {
        var uploadId = partService.create();
        // create dir for uploading
        Files.createDirectory(tempPath.resolve(uploadId));
        return uploadId;
    }

    @Override
    public String putPart(InputStream stream, String uploadId, int part) throws IOException {
        var parts = partService.get(uploadId);
        // 打开文件
        var partsPath = tempPath.resolve(uploadId);
        var partPath = partsPath.resolve(Integer.toString(part));
        var partStream = new DigestOutputStream(Files.newOutputStream(partPath), digest);
        // 开始写入
        stream.transferTo(partStream);
        // 调整名称
        partStream.close();
        var md5 = HexFormat.of().formatHex(partStream.getMessageDigest().digest());
        Files.move(partPath, partsPath.resolve(md5));
        parts.put(part, md5);
        return md5;
    }

    @Override
    public void abortUpload(String uploadId) throws IOException {
        terminalUpload(uploadId);
    }

    private void terminalUpload(String uploadId) throws IOException {
        partService.delete(uploadId);
        deleteParts(uploadId);
    }

    @Override
    public String completeUpload(String uploadId) throws IOException {
        var parts_ = partService.get(uploadId);
        var partsPath = tempPath.resolve(uploadId);
        // 创建主文件
        var combinedPath = partsPath.resolve("combined");
        var combinedStream = new DigestOutputStream(
                Files.newOutputStream(combinedPath), this.digest
        );
        // 将文件进行组合
        for (Map.Entry<Integer, String> entry : parts_.entrySet()) {
            var part = Files.newInputStream(partsPath.resolve(entry.getValue()));
            part.transferTo(combinedStream);
            part.close();
        }
        // 关闭主文件并移动
        combinedStream.close();
        var md5 = HexFormat.of().formatHex(combinedStream.getMessageDigest().digest());
        Files.move(combinedPath, rootPath.resolve(md5), StandardCopyOption.REPLACE_EXISTING);
        terminalUpload(uploadId);
        // TODO：删除temp文件
        return md5;
    }

    @Override
    public InputStream getObject(String etag) throws IOException {
        var objectPath = rootPath.resolve(etag);
        if (!objectPath.toFile().exists()) {
            throw new NilObjectException(etag);
        }
        return Files.newInputStream(objectPath);
    }

    public void deleteParts(String uploadId) throws IOException {
        var partsPath = tempPath.resolve(uploadId);
        Files.walkFileTree(partsPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
        Files.delete(partsPath);
    }
}
