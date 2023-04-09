package top.shlande.clouddisk.user.jdbc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdbcUserRepository extends CrudRepository<JdbcUser, String> {
}
