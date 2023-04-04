package top.shlande.clouddisk.user;

public class DenyException extends RuntimeException {
    public static String createUserAction = "createUser";
    public static String deleteUserAction = "deleteUser";
    public static String updateUserAction = "updateUser";

    public DenyException(String operator, String action) {
        super("access deny, operator:" + operator + ", action:" + action);
    }
}
