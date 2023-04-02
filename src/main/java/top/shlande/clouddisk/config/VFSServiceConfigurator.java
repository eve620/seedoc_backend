package top.shlande.clouddisk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.shlande.clouddisk.vfs.MemoryVFSService;
import top.shlande.clouddisk.vfs.VFSService;
import top.shlande.clouddisk.vfs.mysql.MySQLFileInfoRepository;
import top.shlande.clouddisk.vfs.mysql.MySQLVFSService;

@Configuration
public class VFSServiceConfigurator {
    @Bean
    public VFSService MySQLVFSService(@Autowired MySQLFileInfoRepository repository) {
        return new MySQLVFSService(repository);
    }
}
