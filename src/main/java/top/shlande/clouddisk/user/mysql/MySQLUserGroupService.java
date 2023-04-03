package top.shlande.clouddisk.user.mysql;

import top.shlande.clouddisk.user.UserContext;
import top.shlande.clouddisk.user.UserGroup;
import top.shlande.clouddisk.user.UserGroupService;

public class MySQLUserGroupService implements UserGroupService {
    @Override
    public Integer create(UserContext context) {
        return null;
    }

    @Override
    public void set(Integer id, UserContext context) {

    }

    @Override
    public UserGroup get(String groupId) {
        return null;
    }

    @Override
    public void delete(Integer groupId) {

    }
}
