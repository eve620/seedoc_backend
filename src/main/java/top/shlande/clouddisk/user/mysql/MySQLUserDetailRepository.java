package top.shlande.clouddisk.user.mysql;

import org.springframework.beans.factory.annotation.Autowired;
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
        var user = this.repository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(userId);
        }
        return user.get().toUserDetail();
    }

    @Override
    public void save(UserDetail user) {
        this.repository.save(new MySQLUserDetail(user));
    }

    @Override
    public void delete(String userId) {
        this.repository.deleteById(userId);
    }
}
