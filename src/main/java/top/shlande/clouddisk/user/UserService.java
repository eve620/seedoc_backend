package top.shlande.clouddisk.user;

public class UserService {
    private UserDetailService userDetailService;
    private UserGroupService groupService;

    // 创建用户,只允许管理员操作
    public UserDetail addUser(String operator, String name, String groupId, UserRole role) {
        var user = userDetailService.getUser(operator);
        // 必须保证 group/role 存在
        var group = groupService.get(groupId);
        var newUser = user.createUser(name, group, role);
        userDetailService.save(newUser);
        return newUser;
    }

    // 删除用户， 只允许管理员操作
    public void deleteUser(String operator, String userId) {
        var user = userDetailService.getUser(operator);
        var deleted = userDetailService.getUser(userId);
        if (!user.canDelete(deleted)) {
            throw new DenyException(operator, DenyException.deleteUserAction);
        }
        userDetailService.delete(userId);
    }

    // 设置用户信息，只允许管理员操作
    public void updateUser(String operator, String name, String context) {

    }

    // 设置用户组,只允许全局管理员操作
    public void addUserToGroup(String operator, String userId, String groupId) {

    }
}
