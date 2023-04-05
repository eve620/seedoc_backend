package top.shlande.clouddisk.user;

public interface UserDetailRepository {
    // 查找用户信息
    public UserDetail get(String userId);

    // 添加新用户
    public void create(UserDetail user);

    public void update(UserDetail user);

    // 删除用户
    public void delete(String userId);
}
