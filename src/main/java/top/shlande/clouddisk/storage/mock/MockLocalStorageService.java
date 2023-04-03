package top.shlande.clouddisk.storage.mock;

import top.shlande.clouddisk.storage.CompleteUploadResult;
import top.shlande.clouddisk.storage.LocalStorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockLocalStorageService implements LocalStorageService {
    private final static byte[] stream = new byte[]{74, 65, 73, 74};

    @Override
    public String createUpload() {
        return "test";
    }

    @Override
    public String putPart(InputStream stream, String uploadId, int part) {
        return uploadId;
    }

    @Override
    public void abortUpload(String uploadId) throws IOException {
    }

    @Override
    public CompleteUploadResult completeUpload(String uploadId) {
        return new CompleteUploadResult("test",1000L);
    }

    @Override
    public InputStream getObject(String etag) {
        return new ByteArrayInputStream(stream);
    }
}
