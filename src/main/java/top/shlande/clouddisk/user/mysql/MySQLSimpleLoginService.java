package top.shlande.clouddisk.user.mysql;

import top.shlande.clouddisk.user.NotFoundException;
import top.shlande.clouddisk.user.SimpleLoginService;

import java.util.Objects;

public class MySQLSimpleLoginService implements SimpleLoginService {
    private SpringMySQLUserDetailRepository repository;

    public MySQLSimpleLoginService(SpringMySQLUserDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public String login(String userId, String password) {
        var user = this.repository.get(userId);
        if (user == null) {
            throw new NotFoundException(userId);
        }
        if (!Objects.equals(user.password, password)) {
            return userId;
        }
        // TODO：密码错误
        throw new NotFoundException(userId);
    }

    @Override
    public void update(String userId, String password) {
        this.repository.updatePasswordById(userId, password);
    }
}
