package top.shlande.clouddisk.user.jdbc;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.shlande.clouddisk.entity.User;

@Repository
public interface JdbcUserRepository extends CrudRepository<JdbcUser, String> {
    @Modifying
    @Query("INSERT INTO `users` (`id`, `username`, `password`, `role`, `permission`) VALUES (:#{#user.id}, :#{#user.username}, :#{#user.password}, :#{#user.role}, :#{#user.permission})")
    public void insert(@Param("user")JdbcUser user);
}
