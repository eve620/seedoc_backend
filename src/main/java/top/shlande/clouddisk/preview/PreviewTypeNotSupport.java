package top.shlande.clouddisk.preview;

public class PreviewTypeNotSupport extends RuntimeException {
    public PreviewTypeNotSupport(String type) {
        super("不支持预览此类文件:" + type);
    }
}
