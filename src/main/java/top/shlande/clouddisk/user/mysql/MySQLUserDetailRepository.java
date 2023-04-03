package top.shlande.clouddisk.user.mysql;

import org.springframework.data.repository.CrudRepository;

public interface MySQLUserDetailRepository extends CrudRepository<String, MySQLUserDetail> {

}
