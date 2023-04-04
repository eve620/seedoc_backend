package top.shlande.clouddisk.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserDetailRepository userRepository;
    private UserGroupRepository groupRepository;
    private SimpleLoginService loginService;

    public UserService(@Autowired UserDetailRepository userRepository, @Autowired UserGroupRepository groupRepository, @Autowired SimpleLoginService loginService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.loginService = loginService;
    }

    // 创建用户,只允许管理员操作
    public UserDetail addUser(String operator, String name, String groupId, UserRole role) {
        var user = userRepository.get(operator);
        // 必须保证 group/role 存在
        var group = groupRepository.get(groupId);
        var newUser = user.createUser(name, group, role);
        userRepository.save(newUser);
        return newUser;
    }

    // 删除用户， 只允许管理员操作
    public void deleteUser(String operator, String userId) {
        var user = userRepository.get(operator);
        var deleted = userRepository.get(userId);
        if (!user.canDelete(deleted)) {
            throw new DenyException(operator, DenyException.deleteUserAction);
        }
        userRepository.delete(userId);
    }

    // 设置用户信息，只允许管理员操作
    public void setUser(String operatorId, String userId, String name, String context, String groupId, UserRole role) {
        var operator = userRepository.get(operatorId);
        var user = userRepository.get(userId);
        var group = groupRepository.get(groupId);
        operator.update(user, name, group, role, new UserContext(context));
        userRepository.save(user);
    }

    // 修改用户密码
    public void setPassword(String operatorId, String userId, String newPassword) {
        var operator = userRepository.get(operatorId);
        var user = userRepository.get(userId);
        operator.setCredential(user);
        loginService.update(userId, newPassword);
        // TODO: 注销所有的会话信息
    }

    // 添加用户组，只允许管理员操作
    public String addGroup(String operatorId, String name, String context) {
        var operator = userRepository.get(operatorId);
        var newGroup = new UserGroup(operator, name, new UserContext(context));
        groupRepository.save(newGroup);
        return newGroup.id;
    }

    // 设置group信息，只有全局管理员才能操作
    public void setGroup(String operatorId, String groupId, String name, String context) {
        var operator = userRepository.get(operatorId);
        var group = groupRepository.get(groupId);
        group.edit(operator, name, new UserContext(context));
        groupRepository.save(group);
    }

    // 删除用户组，只允许管理员操作
    public void deleteGroup(String operatorId, String groupId) {
        var operator = userRepository.get(operatorId);
        var group = groupRepository.get(groupId);
        group.delete(operator);
        this.groupRepository.delete(groupId);
    }
}
