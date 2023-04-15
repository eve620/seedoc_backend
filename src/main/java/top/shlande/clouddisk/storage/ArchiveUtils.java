package top.shlande.clouddisk.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveUtils {
    // After calling this method, all stream will be closed
    // path 不应该以 / 结尾，如果是文件夹则 InputStream 为空
    public static void tar(Map<String, Optional<InputStream>> files, OutputStream outputStream) throws IOException {
        var output = new ZipOutputStream(outputStream);
        for (var entry : files.entrySet()) {
            var path = entry.getKey();
            if (entry.getValue().isEmpty()) {
                path += "/";
            }
            var zipEntry = new ZipEntry(path);
            output.putNextEntry(zipEntry);
            var input = entry.getValue();
            if (input.isEmpty()) {
                continue;
            }
            input.get().transferTo(output);
            input.get().close();
        }
        output.close();
    }
}
