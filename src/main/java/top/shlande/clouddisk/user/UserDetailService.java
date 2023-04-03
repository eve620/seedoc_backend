package top.shlande.clouddisk.user;

public interface UserDetailService {
    // 查找用户信息
    public UserDetail getUser(String userId);

    // 添加新用户
    public void save(UserDetail user);

    // 添加用户到群组
    public void add(String user, String group);

    // 添加用户到指定context中
    public void setUser(String context);

    // 删除用户
    public void delete(String userId);
}
