package top.shlande.clouddisk.user.mysql;

import top.shlande.clouddisk.user.UserDetail;
import top.shlande.clouddisk.user.UserDetailRepository;

public class MySQLUserDetailRepository implements UserDetailRepository {
    @Override
    public UserDetail get(String userId) {
        return null;
    }

    @Override
    public void save(UserDetail user) {

    }

    @Override
    public void delete(String userId) {

    }
}
