package top.shlande.clouddisk.user.mysql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import top.shlande.clouddisk.user.UserContext;
import top.shlande.clouddisk.user.UserDetail;
import top.shlande.clouddisk.user.UserRole;

@Table("user")
public class MySQLUserDetail {
    @Id
    public String id;
    public String group;
    // https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#jdbc.entity-persistence.types
    public UserRole role;
    public String password = null;
    public String name;
    public String context;

    public UserDetail toUserDetail() {
        return new UserDetail(this.id, this.group, this.role, this.name, new UserContext(this.context));
    }

    public MySQLUserDetail(UserDetail detail) {
        this.id = detail.id;
        this.group = detail.group;
        this.role = detail.role;
        this.name = detail.name;
        this.context = detail.context.toString();
    }
}
