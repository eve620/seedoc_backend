package top.shlande.clouddisk.user.mysql;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.shlande.clouddisk.user.UserRole;

@Repository
public interface SpringMySQLUserGroupRepository extends CrudRepository<MysqlUserGroup, String> {
    @Modifying
    @Query("INSERT INTO `groups` (id,name,context) VALUES ( :groupId, :name, :context)")
    public void insert(@Param("groupId") String groupId, @Param("name") String name, @Param("context") String context);
}
