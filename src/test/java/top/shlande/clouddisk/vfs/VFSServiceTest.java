package top.shlande.clouddisk.vfs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

@SpringBootTest
public class VFSServiceTest {
    @Autowired
    private VFSService service;

    private final String adminId = "admin";

    private final static FileInfo testFile1 = new FileInfo();
    private final static FileInfo testFile2 = new FileInfo();
    private final static String testFile1Etag1 = UUID.randomUUID().toString();
    private final static String testFile1Etag2 = UUID.randomUUID().toString();
    private final static FileInfo testDir1 = new FileInfo();


    public VFSServiceTest() {
        testFile1.etag = testFile1Etag1;
        testFile1.name = "file";
        testFile1.uploadId = UUID.randomUUID().toString();
        testFile1.owner = adminId;

        testFile2.etag = testFile1Etag2;
        testFile2.name = "file";
        testFile2.uploadId = UUID.randomUUID().toString();
        testFile2.owner = adminId;

        testDir1.name = "dir";
        testDir1.owner = adminId;
    }


    @Test
    public void test() {
        // 在测试前，所有的数据都需要被清空
        // 如果parent文件夹不存在，则报错
        var nonExistDir = "/a/fake/dir/path";
        var gotError = false;
        try {
            service.create(nonExistDir, testFile1);
        } catch (NilDirException dirException) {
            gotError = true;
        }
        Assert.isTrue(gotError, "should fail to create file in a non exist dir");
        // 在根目录创建文件夹
        service.create("", testFile1);
        service.create("", testDir1);
        // 完成上传
        // 在文件夹下创建文件
        service.create(testDir1.name, testFile1);
        // 重复创建文件，会覆盖
        service.create("", testFile2);
        var fileInfo = service.get(testFile1.name);
        Assert.isTrue(fileInfo != null && Objects.equals(fileInfo.etag, testFile2.etag), "should cover duplicated file");
        // 标记文件上传完成
        service.complete(testFile2.uploadId, testFile1Etag1, 100L);
        var result = service.getByUploadId(fileInfo.uploadId);
        Assert.isTrue(result.etag.equals(testFile1Etag1) && result.size == 100L, "object info should be updated");
        // 查看根目录文件，应该是二
        Assert.isTrue(service.list("", 50, 0).size() == 2, "should be 2 object in root path");
        // 查看文件夹文件，应该是一
        Assert.isTrue(service.list(testDir1.name, 50, 0).size() == 1, "should be 1 object in testDir path");
        // 列出文件
        Assert.isTrue(service.walk("").size() == 3, "should be 3 object in fs");
        // 删除文件
        service.delete(testDir1.name + "/" + testFile1.name);
        Assert.isTrue(service.list(testDir1.name, 50, 0).size() == 0, "should be 0 object in testDir path");
        // 删除根目录
        service.delete("");
        Assert.isTrue(service.list("", 50, 0).size() == 0, "should be 0 object in testDir path");
    }

    @Test
    public void testMove() {
        var dst = "教学办公室/2023年/制度文";
        var src = "教学办公室/2023年/制度文件";
        this.service.rename(src,dst);
        Assert.isTrue(this.service.list(src,50,0).size() == 0, "shoould have file");
        Assert.isTrue(this.service.list(dst,50,0).size() > 0, "shoould have file");
        // 还原文件
        this.service.rename(dst,src);
    }
}
