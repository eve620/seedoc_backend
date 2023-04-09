package top.shlande.clouddisk.user;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.springframework.stereotype.Service;
import top.shlande.clouddisk.entity.User;
import top.shlande.clouddisk.user.jdbc.JdbcUser;
import top.shlande.clouddisk.user.jdbc.JdbcUserRepository;

import java.util.Objects;

@Service
public class UserService {
    private JdbcUserRepository repository;

    public UserService(JdbcUserRepository repository) {
        this.repository = repository;
    }

    public User user(String userId) {
        return getUser(userId);
    }

    // 创建用户,只允许管理员操作
    public User addUser(String operator, String name, String password, SimpleRole role, Permission permission) {
        var userOptional = repository.findById(operator);
        if (userOptional.isEmpty()) {
            throw new DenyException(operator, "operator not found");
        }
        var user = userOptional.get().toUserDetail();
        var newUser = user.createUser(name, password, role, permission);
        repository.save(new JdbcUser(newUser));
        return newUser;
    }

    // 删除用户， 只允许管理员操作
    public void deleteUser(String operatorId, String userId) {
        var operatorOptional = repository.findById(operatorId);
        var deletedOptional = repository.findById(userId);
        if (operatorOptional.isEmpty() || deletedOptional.isEmpty()) {
            throw new NotFoundException("");
        }
        var operator = operatorOptional.get().toUserDetail();
        var deleted = deletedOptional.get().toUserDetail();
        if (!operator.canDelete(deleted)) {
            throw new DenyException(operatorId, DenyException.deleteUserAction);
        }
        repository.deleteById(userId);
    }

    // 设置用户信息，只允许管理员操作
    public void setUser(String operatorId, String userId, String name, Permission permission, SimpleRole role) {
        var operator = getUser(operatorId);
        var user = getUser(userId);
        if (!operator.canDelete(user)) {
            throw new DenyException(operatorId, "deleteUser");
        }
        operator.update(user, name, role, permission);
    }

    // 修改用户密码
    public void setPassword(String operatorId, String userId, String newPassword) {
        var operator = getUser(operatorId);
        var user = operator;
        if (!Objects.equals(operatorId, userId)) {
            user = getUser(userId);
        }
        if (!operator.canSetCredential(user)) {
            throw new DenyException(operatorId, "setPassword");
        }
        user.password = newPassword;
        this.repository.save(new JdbcUser(user));
    }

    private User getUser(String userId) {
        var userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(userId);
        }
        return userOptional.get().toUserDetail();
    }
}
