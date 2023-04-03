package top.shlande.clouddisk.user;

public class BadRequestException extends RuntimeException{
    public static String deleteUser = "deleteUser";
    public BadRequestException(String operator, String action) {
        super("bad request, operator:" + operator + ", action:" + action);
    }
}
