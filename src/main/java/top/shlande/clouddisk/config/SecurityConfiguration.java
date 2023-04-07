package top.shlande.clouddisk.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
// https://shiro.apache.org/spring-boot.html#web_applications
public class SecurityConfiguration {
    // Realm is a bridge for shiro to access security data
    // https://www.infoq.com/articles/apache-shiro/
    @Bean
    public Realm realm(@Autowired DataSource dataSource) {
        var realm = new JdbcRealm();
        realm.setDataSource(dataSource);
        realm.setName("ShiroDefaultMySQLRealm");
        realm.setSaltStyle(JdbcRealm.SaltStyle.COLUMN);
        return realm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilter() {
        var chainDefine = new DefaultShiroFilterChainDefinition();
        chainDefine.addPathDefinition("/meta*","");
        chainDefine.addPathDefinition("/file*","");
        return chainDefine;
    }

}
