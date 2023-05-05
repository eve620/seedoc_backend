package top.shlande.clouddisk.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.shlande.clouddisk.archive.ArchiveService;
import top.shlande.clouddisk.archive.ArchiveUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class ArchiveTest {
    @Autowired
    private ArchiveService archiveService;

    private static final Map<String, Optional<InputStream>> files = Map.ofEntries(
            Map.entry("文件夹1/测试文件1.txt", Optional.of(new ByteArrayInputStream("测试文件1".getBytes()))),
            Map.entry("测试文件2.txt", Optional.of(new ByteArrayInputStream("测试文件2".getBytes()))),
            Map.entry("测试文件夹", Optional.empty())
    );

//    @Test
    public void test() throws IOException {
        var output = Files.newOutputStream(Path.of("./test.zip"));
        ArchiveUtils.tar(files, output);
    }

    @Test
    public void testService() throws IOException {
        var output = Files.newOutputStream(Path.of("./test-dir.zip"));
        archiveService.archive(List.of(""), output);
    }

//    @Test
    public void testArchiveFile() throws IOException {
        var output = Files.newOutputStream(Path.of("./test-file.zip"));
        archiveService.archive(List.of("电子信息工程/8f1c5abc129d8c3ef09ffce5de94ac44Q7.png"),output);
    }
}
