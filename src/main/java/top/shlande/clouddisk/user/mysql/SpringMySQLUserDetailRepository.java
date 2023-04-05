package top.shlande.clouddisk.user.mysql;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.shlande.clouddisk.user.UserRole;

import java.util.Optional;

@Repository
public interface SpringMySQLUserDetailRepository extends CrudRepository<MySQLUserDetail,String> {
    // https://stackoverflow.com/questions/43780226/spring-data-ignore-parameter-if-it-has-a-null-value
    // 因为需要公用password，所以这里需要忽略password中断null
    @Modifying
    @Query("INSERT INTO `users` (id,group,role,name,context) VALUES ( :userId, :group, :role, :name, :context)")
    public void create(@Param("userId") String userId, @Param("group") String group, @Param("role") UserRole role, @Param("name") String name, @Param("context") String context);

    @Query("SELECT * FROM `users` WHERE id = :userId LIMIT 1")
    public MySQLUserDetail get(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE `users` SET group = :group, role = :role, name = :name, context = :context WHERE id = :userId")
    public void update(@Param("userId") String userId, @Param("group") String group, @Param("role") UserRole role, @Param("name") String name, @Param("context") String context);

    @Modifying
    @Query("DELETE FROM `users` WHERE id = :userId")
    public void delete(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE `users` SET password = :password WHERE id = :userId")
    public void updatePasswordById(@Param("userId") String userId, @Param("password") String password);
}
