package top.shlande.clouddisk.config;

import org.springframework.data.repository.CrudRepository;
import top.shlande.clouddisk.entity.Config;

public interface ConfigRepository extends CrudRepository<Config, String> {
}