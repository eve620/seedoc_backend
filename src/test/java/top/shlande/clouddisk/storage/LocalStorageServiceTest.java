package top.shlande.clouddisk.storage;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import top.shlande.clouddisk.storage.local.LocalStorageServiceImpl;
import top.shlande.clouddisk.storage.part.MemoryPartService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LocalStorageServiceTest {
    private final LocalStorageService storageService;

    public LocalStorageServiceTest() throws IOException, NoSuchAlgorithmException {
        this.storageService = new LocalStorageServiceImpl("./storage",new MemoryPartService());
    }

    @Test
    public void testTwice() throws Exception {
        // run twice to ensure duplicate object is allowed
        test();
        test();
    }

    @Test
    public void test() throws Exception {
        var uploadId = storageService.createUpload();
        storageService.putPart(new ByteArrayInputStream("world".getBytes()), uploadId, 2);
        storageService.putPart(new ByteArrayInputStream("hello".getBytes()), uploadId, 1);
        var objectId = storageService.completeUpload(uploadId);
        var result = new String(storageService.getObject(objectId.etag).readAllBytes());
        Assert.isTrue(result.equals("helloworld"), "got" + result);
    }


}
