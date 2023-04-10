package top.shlande.clouddisk.user.jdbc;

import lombok.NoArgsConstructor;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import top.shlande.clouddisk.entity.Permissions;
import top.shlande.clouddisk.entity.User;


@Table("users")
@NoArgsConstructor
public class JdbcUser {
    public enum Role {
        ADMIN,
        USER,
    }
    @Id
    public String id;
    public String username;
    public String password;
    public String passwordSalt;
    public String role;
    public String permission;

    public JdbcUser(User detail) {
        this.id = detail.id;
        this.username = detail.name;
        this.password = detail.password;
        switch (detail.role.getName()) {
            case Permissions.userRoleName -> {
                this.role = "USER";
            }
            case Permissions.adminRoleName -> {
                this.role = "ADMIN";
            }
        }
        this.permission = detail.permission.toString();
    }

    public User toUserDetail() {
        SimpleRole role = null;
        switch (this.role) {
            case "USER" -> {
                role = Permissions.user;
            }
            case "ADMIN" -> {
                role = Permissions.admin;
            }
        }
        return new User(
                this.id, this.username, this.password, role, new WildcardPermission(this.permission)
        );
    }
}
