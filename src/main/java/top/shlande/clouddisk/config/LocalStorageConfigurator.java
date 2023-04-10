package top.shlande.clouddisk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.storage.local.LocalStorageServiceImpl;
import top.shlande.clouddisk.storage.mock.MockLocalStorageService;
import top.shlande.clouddisk.storage.part.MemoryPartService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class LocalStorageConfigurator {
    // only in dev environment
    @Bean
    public LocalStorageService mocklocalStorageService() throws IOException, NoSuchAlgorithmException {
        return new LocalStorageServiceImpl("./storage",new MemoryPartService());
    }
}
