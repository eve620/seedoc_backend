package top.shlande.clouddisk.entity;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import top.shlande.clouddisk.user.BadRequestException;

@Table("config")
@NoArgsConstructor
public class Config {
    @Id
    public String variable;

    public String value;

    public void setValue(String value) {
        this.value = value;
    }
}
