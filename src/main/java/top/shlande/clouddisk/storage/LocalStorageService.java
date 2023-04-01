package top.shlande.clouddisk.storage;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

@Service
public interface LocalStorageService {
    // 创建上传分片请求
    public String createUpload() throws Exception;

    // 上传分片
    public String putPart(InputStream stream, String uploadId, int part) throws IOException;

    public void abortUpload(String uploadId) throws IOException;

    // 完成分片
    public String completeUpload(String uploadId) throws IOException, NoSuchAlgorithmException;

    // 下载文件
    public InputStream getObject(String etag) throws IOException;
}
