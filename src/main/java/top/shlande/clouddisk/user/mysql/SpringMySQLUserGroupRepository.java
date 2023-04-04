package top.shlande.clouddisk.user.mysql;

import org.springframework.data.repository.CrudRepository;

public interface SpringMySQLUserGroupRepository extends CrudRepository<String, MysqlUserGroup> {
}
