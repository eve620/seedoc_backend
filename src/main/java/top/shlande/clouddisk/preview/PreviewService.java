package top.shlande.clouddisk.preview;

import java.io.InputStream;

// 从本质上来来说，Preview只将文件转化为PDF，然后返回一个OutputStream
public interface PreviewService {
    // 尝试获取一个PDF stream，如果没有则返回nil
    InputStream get(String checksum);
    // 生成一个Preview,返回PDF stream
    // 如果类型不支持，throw
    InputStream create(String checksum,String type, InputStream stream);
}
