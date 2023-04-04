package top.shlande.clouddisk.user;

// 群组管理
public interface UserGroupRepository {
    // 设置群组权限
    public void save(UserGroup context);

    // 获取群组详情
    public UserGroup get(String groupId);

    // 删除群组
    // 在删除群组前，必须让用户解除绑定
    public void delete(String groupId);
}
