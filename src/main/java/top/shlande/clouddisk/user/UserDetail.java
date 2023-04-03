package top.shlande.clouddisk.user;

import lombok.Data;

import java.util.List;

@Data
public class UserDetail {
    public Integer id;
    public Integer group;
    public String name;
    public String avatar;
    // 用户能够访问的上下文
    public List<UserContext> context;
}
