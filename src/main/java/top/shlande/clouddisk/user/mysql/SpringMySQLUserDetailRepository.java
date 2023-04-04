package top.shlande.clouddisk.user.mysql;

import org.springframework.data.repository.CrudRepository;

public interface SpringMySQLUserDetailRepository extends CrudRepository<String, MySQLUserDetail> {

}
