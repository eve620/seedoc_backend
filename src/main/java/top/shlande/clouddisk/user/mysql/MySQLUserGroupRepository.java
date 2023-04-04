package top.shlande.clouddisk.user.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import top.shlande.clouddisk.user.NotFoundException;
import top.shlande.clouddisk.user.UserGroup;
import top.shlande.clouddisk.user.UserGroupRepository;

public class MySQLUserGroupRepository implements UserGroupRepository {
    private final SpringMySQLUserGroupRepository repository;

    public MySQLUserGroupRepository(@Autowired SpringMySQLUserGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(UserGroup group) {
        repository.save(new MysqlUserGroup(group));
    }

    @Override
    public UserGroup get(String groupId) {
        var group = repository.findById(groupId);
        if (group.isEmpty()) {
            throw new NotFoundException(groupId);
        }
        return group.get().toGroup();
    }

    @Override
    public void delete(String groupId) {
        repository.deleteById(groupId);
    }
}
