package top.shlande.clouddisk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.shlande.clouddisk.user.UserService;
import top.shlande.clouddisk.user.jdbc.JdbcUserRepository;

@Configuration
public class UserServiceConfiguration {

    @Bean
    public UserService userDetailRepository(@Autowired JdbcUserRepository repository) {
        return new UserService(repository);
    }
}
