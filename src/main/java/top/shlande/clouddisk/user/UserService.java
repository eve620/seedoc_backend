package top.shlande.clouddisk.user;

import top.shlande.clouddisk.entity.User;
import top.shlande.clouddisk.entity.UserContext;
import top.shlande.clouddisk.user.jdbc.JdbcUser;
import top.shlande.clouddisk.user.jdbc.JdbcUserRepository;

import java.util.*;

public class UserService {
    private JdbcUserRepository repository;

    public UserService(JdbcUserRepository repository) {
        this.repository = repository;
    }

    public List<User> listByIds(List<String> ids) {
        var result = new ArrayList<User>(ids.size());
        this.repository.findAllById(ids).forEach(user -> {
            result.add(user.toUserDetail());
        });
        return result;
    }

    public List<User> listAll() {
        var result = new LinkedList<User>();
        this.repository.findAll().forEach(user -> {
            result.add(user.toUserDetail());
        });
        return result;
    }

    public User user(String userId) {
        return getUser(userId);
    }

    // 创建用户,只允许管理员操作
    public User addUser(String operator, String userId, String name, String password, User.Role role, UserContext permission) {
        var userOptional = repository.findById(operator);
        if (userOptional.isEmpty()) {
            throw new DenyException(operator, "operator not found");
        }
        var user = userOptional.get().toUserDetail();
        var newUser = user.createUser(userId, name, password, role, permission);
        repository.insert(new JdbcUser(newUser));
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
    public void setUser(String operatorId, String userId, String name, String password, UserContext permission, User.Role role) {
        var operator = getUser(operatorId);
        var user = getUser(userId);
        if (!operator.canSetCredential(user)) {
            throw new DenyException(operatorId, "deleteUser");
        }
        operator.update(user, name, password, role, permission);
        this.repository.save(new JdbcUser(user));
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

    public boolean login(String userId, String password) {
        var user = getUser(userId);
        return Objects.equals(user.password, password);
    }
}
