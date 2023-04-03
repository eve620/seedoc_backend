package top.shlande.clouddisk.user;

// 群组管理
public interface UserGroupService {
    // 创建群组
    public Integer create(UserContext context);

    // 设置群组权限
    public void set(Integer id, UserContext context);

    // 获取群组详情
    public UserGroup get(String groupId);

    // 删除群组
    // 在删除群组前，必须让用户解除绑定
    public void delete(Integer groupId);
}
