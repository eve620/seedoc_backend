package top.shlande.clouddisk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.shlande.clouddisk.user.SimpleLoginService;
import top.shlande.clouddisk.user.UserDetailRepository;
import top.shlande.clouddisk.user.UserGroupRepository;
import top.shlande.clouddisk.user.mysql.*;

@Configuration
public class UserServiceConfiguration {

    @Bean
    public UserDetailRepository userDetailRepository(@Autowired SpringMySQLUserDetailRepository userRepository) {
        return new MySQLUserDetailRepository(userRepository);
    }

    @Bean
    public UserGroupRepository userGroupRepository(@Autowired SpringMySQLUserGroupRepository groupRepository) {
        return new MySQLUserGroupRepository(groupRepository);
    }

    @Bean
    public SimpleLoginService simpleLoginService(@Autowired SpringMySQLUserDetailRepository repository) {
        return new MySQLSimpleLoginService(repository);
    }
}
