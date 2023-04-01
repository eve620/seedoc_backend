package top.shlande.clouddisk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.shlande.clouddisk.storage.LocalStorageService;
import top.shlande.clouddisk.storage.local.LocalStorageServiceImpl;
import top.shlande.clouddisk.storage.mock.MockLocalStorageService;

@Configuration
public class LocalStorageConfigurator {
//    @Bean
//    public LocalStorageService localStorageServiceImpl(String path) {
//        return new LocalStorageServiceImpl(path);
//    }

    // only in dev environment
    @Bean
    public LocalStorageService mocklocalStorageService() {
        return new MockLocalStorageService();
    }
}
