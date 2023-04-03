package top.shlande.clouddisk.user;

// UserAuthService 用来管理当前的用户登录情况
public interface UserSessionService {
    // 通过token获取用户信息
    public String auth(String token);

    // 主动过期token
    public String expire(String token);
}
