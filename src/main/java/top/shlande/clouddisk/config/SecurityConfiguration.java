package top.shlande.clouddisk.config;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Configuration
// https://shiro.apache.org/spring-boot.html#web_applications
public class SecurityConfiguration {
    // 因为只有一个role，所以不单独形成一个表
    private static final String userRoleQuery = "select role from user where username = ?";

    // Realm is a bridge for shiro to access security data
    // https://www.infoq.com/articles/apache-shiro/
    @Bean
    public Realm realm(@Autowired DataSource dataSource) {
        var realm = new JdbcRealm() {
            @Override
            protected Set<String> getPermissions(Connection conn, String username, Collection<String> roleNames) throws SQLException {
                // fix permission
                return new HashSet<>();
            }
        };
        realm.setUserRolesQuery(userRoleQuery);
        realm.setDataSource(dataSource);
        realm.setName("ShiroDefaultMySQLRealm");
        realm.setSaltStyle(JdbcRealm.SaltStyle.COLUMN);
        return realm;
    }

    @Bean
    public SecurityManager securityManager(@Autowired Realm realm) {
        return new DefaultSecurityManager(realm);
    }


    @Bean
    public ShiroFilterChainDefinition shiroFilter() {
        var chainDefine = new DefaultShiroFilterChainDefinition();
        chainDefine.addPathDefinition("/**", "anno");
        return chainDefine;
    }

}
