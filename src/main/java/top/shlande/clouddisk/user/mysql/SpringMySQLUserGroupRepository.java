package top.shlande.clouddisk.user.mysql;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringMySQLUserGroupRepository extends CrudRepository<MysqlUserGroup,String> {
}
