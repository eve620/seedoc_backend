package top.shlande.clouddisk.storage;

import java.io.InputStream;

public interface RemoteStorageWorkerService {
    // 下载文件
    public InputStream getObject(String etag);

    // 上传文件
    public void putPart(InputStream stream, String uploadId, int part);

    // 合并文件
    public void completeUpload(String uploadId);
}
