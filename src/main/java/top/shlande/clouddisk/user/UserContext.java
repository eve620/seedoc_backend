package top.shlande.clouddisk.user;

import org.apache.catalina.User;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class UserContext implements PermissionResolver {
    public static String global = "";
    private static final char separator = '|';
    public Set<String> context;


    public UserContext(String context) {
        context = context == null ? "" : context;
        this.context = new TreeSet<>(List.of(context.split("\\|")));
    }

    public String toString() {
        return Strings.join(this.context.iterator(), separator);
    }

    public UserContext(List<String> context) {
        if (context == null) {
            this.context = new TreeSet<>();
            return;
        }
        this.context = new TreeSet<>(context);
    }

    public boolean canAccess(String path) {
        for (String context : this.context) {
            if (context.length() == 0 || Objects.equals(path, context) || path.indexOf(context + "/") == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Permission resolvePermission(String permissionString) {
        return null;
    }
}
