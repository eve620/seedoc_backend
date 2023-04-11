package top.shlande.clouddisk.entity;

import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class UserContext {
    public final static UserContext global = new UserContext("*");

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

    // TODO: 中文处理问题
    public boolean canAccess(String path) {
        for (String context : this.context) {
            var index = path.indexOf(context + "/");
            if (Objects.equals(context, "*") || Objects.equals(path, context) || (index <= 1 && index >= 0)) {
                return true;
            }
        }
        return false;
    }
}
