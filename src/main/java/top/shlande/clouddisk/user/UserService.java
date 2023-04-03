package top.shlande.clouddisk.user;

public interface UserService {
    // 查找用户信息
    public UserDetail getUser(String userId);

    // 添加用户到群组
    public

    // 添加用户到指定context中
    public void setUser(String context);

    // 删除用户
    public void delete(String userId);

    // 创建群组
    public Integer create(UserContext context);

    // 设置群组权限
    public void set(Integer id, UserContext context);

    // 获取群组详情
    public UserContext get(Integer groupId);

    // 删除群组
    public void delete(Integer groupId);
}
