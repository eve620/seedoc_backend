package top.shlande.clouddisk.user;

import java.util.Set;

public class UserContext {
    public static String global = "";
    public Set<String> context;

    public boolean canAccess(String path) {
        for (String context : this.context) {
            if (path.indexOf(context) == 0) {
                return true;
            }
        }
        return false;
    }
}
