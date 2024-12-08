package top.shlande.clouddisk.entity;

import lombok.Data;

@Data
public class R {
    public Integer code;
    public String message;
    public R() {}
    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
