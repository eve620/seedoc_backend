package top.shlande.clouddisk.storage;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class ArchiveTest {

    private static final Map<String, Optional<InputStream>> files = Map.ofEntries(
            Map.entry("文件夹1/测试文件1.txt", Optional.of(new ByteArrayInputStream("测试文件1".getBytes()))),
            Map.entry("测试文件2.txt", Optional.of(new ByteArrayInputStream("测试文件2".getBytes()))),
            Map.entry("测试文件夹", Optional.empty())
    );
    @Test
    public void test() throws IOException {
        var output = Files.newOutputStream(Path.of("./test.zip"));
        ArchiveUtils.tar(files,output);
    }

}
