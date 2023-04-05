package top.shlande.clouddisk.user.mysql;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringMySQLUserGroupRepository extends CrudRepository<MysqlUserGroup,String> {
}
