package top.shlande.clouddisk.user.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import top.shlande.clouddisk.user.NotFoundException;
import top.shlande.clouddisk.user.UserDetail;
import top.shlande.clouddisk.user.UserDetailRepository;

public class MySQLUserDetailRepository implements UserDetailRepository {
    public MySQLUserDetailRepository(@Autowired SpringMySQLUserDetailRepository repository) {
        this.repository = repository;
    }

    private SpringMySQLUserDetailRepository repository;

    @Override
    public UserDetail get(String userId) {
        var user = this.repository.get(userId);
        if (user == null) {
            throw new NotFoundException(userId);
        }
        return user.toUserDetail();
    }

    @Override
    public void save(UserDetail user) {
        this.repository.update(user.id, user.group, user.role, user.name, user.context.toString());
    }

    @Override
    public void delete(String userId) {
        this.repository.delete(userId);
    }
}
