package top.shlande.clouddisk.storage;

public interface RemoteStorageMasterService {
    // 创建一个上传任务，返回上传地址
    public String createUpload();

    // 标记上传任务完成
    public void completeUpload(String uploadId);

    // 获取文件下载 Url
    public String getObject(String etag);
}
