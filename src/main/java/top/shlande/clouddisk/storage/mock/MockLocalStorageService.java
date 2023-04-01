package top.shlande.clouddisk.storage.mock;

import top.shlande.clouddisk.storage.LocalStorageService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MockLocalStorageService implements LocalStorageService {
    private final static byte[] stream = new byte[]{74, 65, 73, 74};

    @Override
    public String createUpload() {
        return "test";
    }

    @Override
    public void putPart(InputStream stream, String uploadId, int part) {
    }

    @Override
    public String completeUpload(String uploadId) {
        return "test";
    }

    @Override
    public InputStream getObject(String etag) {
        return new ByteArrayInputStream(stream);
    }
}
