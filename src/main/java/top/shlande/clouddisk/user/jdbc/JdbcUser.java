package top.shlande.clouddisk.user.jdbc;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import top.shlande.clouddisk.entity.User;
import top.shlande.clouddisk.entity.UserContext;


@Table("users")
@NoArgsConstructor
public class JdbcUser {
    @Id
    public String id;
    public String username;
    public String password;
    public String role;
    public String permission;

    public JdbcUser(User detail) {
        this.id = detail.id;
        this.username = detail.name;
        this.password = detail.password;
        switch (detail.role) {
            case USER -> {
                this.role = "USER";
            }
            case ADMIN -> {
                this.role = "ADMIN";
            }
        }
        this.permission = detail.context.toString();
    }

    public User toUserDetail() {
        User.Role role = null;
        switch (this.role) {
            case "USER" -> {
                role = User.Role.USER;
            }
            case "ADMIN" -> {
                role = User.Role.ADMIN;
            }
        }
        return new User(
                this.id, this.username, this.password, role, new UserContext(permission)
        );
    }
}
