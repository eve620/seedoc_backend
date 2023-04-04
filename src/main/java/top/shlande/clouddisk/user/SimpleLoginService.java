package top.shlande.clouddisk.user;

public interface SimpleLoginService {
    // 密码登录，获取用户id
    // 如果有错误，必须在内部
    public String login(String userId, String password);

    // 更新密码
    public void update(String userId, String password);
}
