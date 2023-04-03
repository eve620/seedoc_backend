package top.shlande.clouddisk.user;

public interface SimpleLoginService {
    // 密码登录，获取用户id
    public String login(String username, String password);
}
