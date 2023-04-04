package top.shlande.clouddisk.user.mysql;

import top.shlande.clouddisk.user.UserContext;
import top.shlande.clouddisk.user.UserGroup;
import top.shlande.clouddisk.user.UserGroupRepository;

public class MySQLUserGroupRepository implements UserGroupRepository {
    private SpringMySQLUserGroupRepository repository;

    @Override
    public void save(UserContext context) {

    }

    @Override
    public UserGroup get(String groupId) {
        return null;
    }

    @Override
    public void delete(String groupId) {

    }
}
