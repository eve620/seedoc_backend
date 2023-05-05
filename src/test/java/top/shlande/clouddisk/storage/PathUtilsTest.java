package top.shlande.clouddisk.storage;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import top.shlande.clouddisk.PathUtils;

public class PathUtilsTest {
    @Test
    public void name() {
        Assert.isTrue(PathUtils.filename("文件夹").equals("文件夹"),"should equal");
        Assert.isTrue(PathUtils.filename("/文件夹").equals("文件夹"),"should equal");
        Assert.isTrue(PathUtils.filename("问你/文件夹").equals("文件夹"),"should equal");
        Assert.isTrue(PathUtils.filename("/").equals(""),"should equal");
        Assert.isTrue(PathUtils.filename("").equals(""),"should equal");
    }

    @Test void directory() {
        Assert.isTrue(PathUtils.directory("文件夹").equals(""),"should equal");
        Assert.isTrue(PathUtils.directory("/文件夹").equals(""),"should equal");
        Assert.isTrue(PathUtils.directory("问你/文件夹").equals("问你"),"should equal");
        Assert.isTrue(PathUtils.directory("/").equals("/"),"should equal");
        Assert.isTrue(PathUtils.directory("").equals(""),"should equal");
    }
}
